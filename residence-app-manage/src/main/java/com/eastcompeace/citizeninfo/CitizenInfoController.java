package com.eastcompeace.citizeninfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.model.CitizenInfoModel;
import com.eastcompeace.model.CitizenPhotoInfoModel;
import com.eastcompeace.share.utils.DirectoryUtils;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ImageUtils;
import com.eastcompeace.util.ResidenceUtils;
import com.eastcompeace.util.ResourceUtils;


/**
 * 流口输入导入
 * @author panyanlin
 *
 */
@Controller
@RequestMapping("/citizenImpctrl")
public class CitizenInfoController extends BaseController {
	private Logger logger_ = Logger.getLogger("CitizenInfoController");

	@Resource
	private SqlSession sqlSession;
	@Resource
	private CitizenInfoDao citizenInfoDao;
	
	/**
	 * 基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView showCitizenIndex() {
		return super.getModelView("citizen/dataImport");
	}
	
	/**
	 * 流口数据导入临时表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 程序业务流程：
	 * 一、保存源文件压缩包到指定目录：
	 * 1）获取上传源文件，指定目录路径；
	 * 2）判断目录是否存在该源文件名，若存在则返回错误提示文件已存在；
	 * 3）转存源文件到指定目录；
	 * 二、解压流口数据xls文件、照片zip文件到指定目录：
	 * 1）分别获取流口数据以及照片数据保存路径；
	 * 2）判断目录是否已经存在需要保存的文件，若存在则删除源文件并返回错误信息；
	 * 3）把流口数据以及照片zip文件分别解压到指定目录；
	 * 三、解析流口数据：
	 * 1）检测数据是否符合规范；
	 * 2）若数据有误则保存错误信息，然后继续检查下面的数据；
	 * 3）每检查完毕一行则把该行数据封装成CitizenInfoModel并存入list里面；
	 * 4）检测完毕所有数据后若中途检测出错误数据则返回错误信息；
	 * 四、解压照片数据，组拼完整流口信息，并上传数据库：
	 * 1）解压并压缩照片为71*99像素；
	 * 2）照片数据与流口数据进行匹配，匹配成功则存入该流口数据的model中并存入List集合；
	 * 3）List集合元素大于或者等于100条时则批量上传到数据库；
	 * 4）若匹配成功的数据与流口数据数量不一致，则根据批次号删除数据库中的数据，并删除保存的文件，并返回错误信息；
	 * 五、删除数据库中的重复数据（身份证号相同，居住证号不同的数据）
	 * 1）查询出重复的数据；
	 * 2）根据制卡日期（importDate）来判断新旧数据；
	 * 3）把旧数据卡片状态设置为“01”注销；
	 * 4）把注销的数据转存入备份表中；
	 * 5）删除正式表中的注销数据。
	 * 
	 */
	@RequestMapping("/impCitizenInfo")
	@ResponseBody
	@Transactional
	public JSONObject impCitizenData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger_.debug("import citizen data start");
		JSONObject jsonObj = new JSONObject();
		/*
		 * 一、保存源文件压缩包到指定目录：
		 */
		//批次号规则 RCO+yyyyMMddHHmmssS  S为毫秒数
		String serialNo = ResidenceUtils.createSerializeNO();
		
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);

		// 获取上传文件
		MultipartFile importFile = muRequest.getFile("zipImportFile");
		if(importFile==null || "".equals(importFile)) {
			jsonObj.put("result", "-1");
			jsonObj.put("message", "请选择需要上传的文件！");
			return jsonObj;
		}
		
		// 获取源文件路径
		String srcFileUploadPath = ResourceUtils.getProperty("citizenImportUrl")+File.separator+"src";
		File upSrcFileFolder = new File(srcFileUploadPath);
		if (!upSrcFileFolder.exists()) {
			upSrcFileFolder.mkdirs();
		}
		//组拼源文件URL
		String srcFileName = srcFileUploadPath + File.separator + importFile.getOriginalFilename();
		// 将上传源文件转存到指定目录下
		File srcFile = new File(srcFileName);
		if (srcFile.exists()) {
			System.gc();
			jsonObj.put("result", "-1");
			jsonObj.put("message", "数据文件已导入，不可以再次导入");
			return jsonObj;
		}
		//转存文件	
		try {
			importFile.transferTo(srcFile);
		} catch (Exception e) {
			System.gc();
			logger_.error("upload citizen data error:"+e.getMessage());
			jsonObj.put("result", "-1");
			jsonObj.put("message", "数据源文件导入失败");
			return jsonObj;
		}
		
		/*
		 *  二、解压流口数据xls文件、照片zip文件到指定目录：
		 */
		// 获取流口数据文件存放目录
		String uploadXlsPath = ResourceUtils.getProperty("citizenImportUrl")+File.separator+"xls";
		File upfilefolder = new File(uploadXlsPath);
		if (!upfilefolder.exists()) {
			upfilefolder.mkdirs();
		}
		
		// 压缩图片与图片保存路径
		String photoSavePath = ResourceUtils.getProperty("citizenImportUrl") + File.separator + "photo"+ 
				File.separator + serialNo;
		File uppicfolder = new File(photoSavePath);
		if (!uppicfolder.exists()) {
			uppicfolder.mkdirs();
		}

		//创建Zip输入流
		InputStream in = new FileInputStream(srcFile);
		ZipInputStream zipInputStream = new ZipInputStream(in,Charset.forName("GBK"));
		ZipEntry zipEntry;
		//定义数据文件，和照片文件名
		String xlsRelaName = null;
		String phoRelaName = null;
		//给ZIP文件赋值
		BufferedOutputStream bos = null;
		BufferedInputStream bis =  null;
		while ((zipEntry = zipInputStream.getNextEntry()) != null) {		
			//解压文件
			String entryFileName = zipEntry.getName();
			//检查文件名是否与源文件名一致
			if(!importFile.getOriginalFilename().substring(0, importFile.getOriginalFilename().lastIndexOf(".zip")).equalsIgnoreCase(entryFileName.substring(0, entryFileName.lastIndexOf("-")))){
				jsonObj.put("result", "-1");
				jsonObj.put("message", "压缩包里的子文件名和压缩包名不一致，请确认！");
				DirectoryUtils.delete(srcFileName);
				zipInputStream.close();
				return jsonObj;
			}
			InputStream entryStream = new ZipFile(srcFile).getInputStream(zipEntry);
			bis = new BufferedInputStream(entryStream);
			File inFile = null;
			//判断源文件压缩包里的文件后缀是否为.xls和.zip，若不是则删除源文件，然后返回错误信息
			if(entryFileName.toUpperCase().endsWith(".XLS")) {
				xlsRelaName = uploadXlsPath + File.separator + zipEntry.getName();
				inFile = new File(xlsRelaName);
			} else if(entryFileName.toUpperCase().endsWith(".ZIP")) {
				phoRelaName = photoSavePath + File.separator + zipEntry.getName();
				inFile = new File(phoRelaName);
			} else {
				jsonObj.put("result", "-1");
				jsonObj.put("message", "上传的压缩文件包含xls,zip以外的文件，请确认");
				DirectoryUtils.delete(srcFileName);
				in.close();
				zipInputStream.close();
				entryStream.close();
				bis.close();			
				return jsonObj;
			}
			//若源文件压缩包里的子文件在本地已存在则，删除源文件，并返回错误信息
			if (inFile.exists()) {
				System.gc();
				jsonObj.put("result", "-1");
				jsonObj.put("message", inFile.getName()+"文件已导入，不可以再次导入");
				in.close();
				zipInputStream.close();
				entryStream.close();
				bis.close();
				DirectoryUtils.delete(srcFileName);
				return jsonObj;
			}
			//将源文件压缩包里的子文件读取到指定目录
			inFile.createNewFile();
			bos = new BufferedOutputStream(new FileOutputStream(inFile));
			byte[] le = new byte[1024];
			int len = 0;
			while((len=bis.read(le)) != -1) {
				bos.write(le,0,len);				
			}
			bos.flush();			
		}	
		in.close();
		zipInputStream.close();
		bos.close();
		bis.close();
		
		/*
		 * 三、解析流口数据：
		 */
		// 流口数据格式检查0 成功 非0失败，并返回流口数据集合citizenInfoList
		File citiXlsFile = new File(xlsRelaName);
		String countStr = xlsRelaName.substring(xlsRelaName.lastIndexOf(")-")+2, xlsRelaName.lastIndexOf("-"));
		if(!countStr.matches("\\d+")) {
			System.gc();
			jsonObj.put("result", "-1");
			jsonObj.put("message", ".xls文件文件名数量字段错误");
			return jsonObj;
		}
		
		Map<String, Object> resMapXls = this.upXlsFile(citiXlsFile,serialNo,Integer.parseInt(countStr));
		//先对象是否属于String
		if( !(resMapXls.get("resCode") instanceof String)) {			
			//删除文件
			DirectoryUtils.delete(photoSavePath);
			DirectoryUtils.delete(xlsRelaName);
			DirectoryUtils.delete(srcFileName);
			//返回错误信息
			jsonObj.put("result", "-1");
			jsonObj.put("message", "系统异常！");
			resMapXls.clear();
			System.gc();
			return jsonObj;
		}
		String xlReCode = (String)resMapXls.get("resCode");
		if (!"0".equals(xlReCode)) {
			//删除文件
			DirectoryUtils.delete(photoSavePath);
			DirectoryUtils.delete(xlsRelaName);
			DirectoryUtils.delete(srcFileName);
			//返回错误信息
			jsonObj.put("result", "-1");
			jsonObj.put("message",  (String)resMapXls.get("resMessage"));
			jsonObj.put("message1", (String)resMapXls.get("resMessage1"));
			resMapXls.clear();
			System.gc();
			return jsonObj;
		}
		//获取解析流口数据返回的流口信息集合
		if( !(resMapXls.get("citizenInfoList") instanceof List)) {
			//删除文件
			DirectoryUtils.delete(photoSavePath);
			DirectoryUtils.delete(xlsRelaName);
			DirectoryUtils.delete(srcFileName);
			//返回错误信息
			jsonObj.put("result", "-1");
			jsonObj.put("message", "系统异常！");
			resMapXls.clear();
			System.gc();
			return jsonObj;
		}
		List<CitizenInfoModel> citizenList = null;
		try {
			citizenList = (List<CitizenInfoModel>)resMapXls.get("citizenInfoList");
		} catch (Exception e) {
			jsonObj.put("result", "-1");
			jsonObj.put("message", "系统异常！");
		}
		//清空resMapXls
		resMapXls.clear();
		/*
		 * 四、解压照片数据，组拼完整流口信息，并上传数据库：
		 */
		// 解压照片并压缩 0 成功 非0失败，将照片与流口数据一一对应写入数据库
		File citiPhoFile = new File(phoRelaName);
		Map<String, String> resMapPho = this.upPhoFile(citiPhoFile, serialNo, photoSavePath, citizenList);
		citizenList.clear();
		String phReCode = resMapPho.get("resCode");
		if (!"0".equals(phReCode)) {
			System.gc();
			//删除文件
			DirectoryUtils.delete(photoSavePath);
			DirectoryUtils.delete(xlsRelaName);
			DirectoryUtils.delete(srcFileName);
			//删除临时表内容
			citizenInfoDao.deleteCitizenData(serialNo);
			jsonObj.put("result", "-1");
			jsonObj.put("message", resMapPho.get("resMessage"));
			return jsonObj;
		}
		System.gc();
		/*
		 * 五、删除数据库中的重复数据（身份证号相同，居住证号不同的数据）	
		 */
		try {
			//查询目标表中身份证重复数据
			List<CitizenInfoModel> citizenInfoList = citizenInfoDao.queryDuplicateData();
			if(citizenInfoList.size()>0) {
				//把身份证重复数据放入rcList集合里中比较旧的数据（根据制卡日期判断）
				List<String> rcList  = new ArrayList<String>();
				for(CitizenInfoModel cm : citizenInfoList) {
					for(CitizenInfoModel cm1 : citizenInfoList) {
						//若身份证号相同，则比较居住证有效期，把制卡日期早的数据放入rcList中
						if(cm1.getIdCard().equals(cm.getIdCard())) {
							if(cm1.getImportDate().compareToIgnoreCase(cm.getImportDate())<0) {
								rcList.add(cm1.getRcCard());
							}
						}
					}
				}				
				//通过重复数据的rc_card居住证号来设置该条数据状态为“01”注销
				citizenInfoDao.updateRcStatus(rcList);
				//把状态为01的数据复制到如此rc_card_info_bak中
				citizenInfoDao.insertDataToBakTab();			
				//删除正式表中状态为“01”的数据
				citizenInfoDao.delDuplicateData();
			}
		} catch (Exception e) {
			logger_.error(this, e);
			jsonObj.put("result", "-1");
			jsonObj.put("message", "数据库异常："+e.getMessage());
			return jsonObj;
		}
		

		jsonObj.put("result", "0");
		jsonObj.put("message", "数据导入成功");
		logger_.debug("import citizen data end");
		//System.out.println("end :" + DateUtils.getNow("HH:mm:ss"));
		return jsonObj;
	}

	/**
	 * 解析流口数据文件 xls
	 * 
	 * @param xlsfile
	 *            流口数据文件
	 * @param serialNo
	 *            序列码
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> upXlsFile(File xlsfile, String serialNo,int recordCount) throws Exception {
		logger_.debug("upload citizen data start");
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		List<CitizenInfoModel> citizenInfoList = new ArrayList<CitizenInfoModel>();
		
		Workbook rwb = Workbook.getWorkbook(xlsfile);
		Sheet rs = rwb.getSheet(0);
		int rows = rs.getRows();// 得到所有的行
		//拼装错误信息返回文件内容 txt
		StringBuffer msg = new StringBuffer(); 
		//数据判断位，如果为true则说明该条记录无问题可以导入数据库
		boolean isInDatabase = true;
		
		//把数据实例化成一个居民信息类，加入List
		for (int i = 1; i < rows; i++) {
			
			CitizenInfoModel citizenInfo = new CitizenInfoModel();	
			Cell[] cells = rs.getRow(i);
			if (cells == null || cells.length == 0) {
				continue;
			}
			
			//按照定义的Excel表（第一位为居住证号）获取居住证卡号
			String rcCard = rs.getCell(0, i).getContents().trim().replace("\t", "");
			//居住证为临时表主键不能为空，若为空则该条数据不获取，继续获取其他条数据
			if(rcCard==null || "".equals(rcCard) || !rcCard.matches("\\d{18}")) {
				msg.append("第"+(i+1)+"行记录居住证为空，该数据不合法，无法导入数据库;<br/>");
				isInDatabase = false;
				continue;
			}
			citizenInfo.setRcCard(rcCard);
			
			/*
			 * 居住证卡类型,从居住证卡号获取，
			 * 规则：
			 * 卡号第9位的（1-9）表示卡类型1-9
			 * 若卡号第9位为0则获取卡号第10位的（1-9）表示卡类型01-09
			 * */
			String rcCardType = "";
			if(rcCard.substring(8, 9).matches("[1-9]")) {
				rcCardType=rcCard.substring(8, 9);
			} else if("0".equals(rcCard.substring(8, 9))){
				rcCardType="0"+ rcCard.substring(10, 11);
			}	
			if("".equals(rcCardType) || !rcCardType.matches("\\d{1,2}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录计算出来的居住证卡类型["+rcCardType+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setRcCardType(rcCardType);
			
			//获取姓名
			String str = rs.getCell(1, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || str.length()>50) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录姓名["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setName(str);
			
			//出生日期
			str = rs.getCell(2, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{8}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录出生日期["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setBirthday(str);
			
			//性别
			str = rs.getCell(3, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{2}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录性别["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setSex(str);
			
			//民族
			str = rs.getCell(4, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{2}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录民族["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setNation(str);
			
			//身份证号
			str = rs.getCell(5, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{14}|\\d{17}[0-9xX]")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录身份证号["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setIdCard(str);
			
			//户籍地区
			str = rs.getCell(6, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || str.length()>100) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录户籍地区["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setAddress(str);
			
			//签发日期
			str = rs.getCell(7, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{8}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录签发日期["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setIssueDate(str);
			
			//签发机关
			str = rs.getCell(8, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || str.length()>100) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录签发机关["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setIssueOffice(str);
			
			//服务热线
			str = rs.getCell(9, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) ) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录服务热线["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setServiceTel(str);
						
			//银行卡号(儿童卡无银行卡号)
			citizenInfo.setBankCard("");
			if(!"5".equals(rcCardType)) {
				str = rs.getCell(10, i).getContents().trim().replace("\t", "");
				if(str==null || "".equals(str) || !str.matches("\\d{15,20}")) {
					msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录银行卡号["+str+"]不合规，无法导入数据库;<br/>");
					isInDatabase = false;
				}
				citizenInfo.setBankCard(str);
			}
			
			//有效期
			str = rs.getCell(11, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{8}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录有效期["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setValidThru(str);
				
			//区域代码
			str = rs.getCell(12, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{6}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录区域代码["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setAreaId(str);
					
			//制卡日期
			str = rs.getCell(13, i).getContents().trim().replace("\t", "");
			if(str==null || "".equals(str) || !str.matches("\\d{8}")) {
				msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的记录制卡日期["+str+"]不合规，无法导入数据库;<br/>");
				isInDatabase = false;
			}
			citizenInfo.setImportDate(str);
			
			/*
			 * 市民卡编号
			 * 目前只有杭州市民卡存在该字段,其余卡款允许为空，格式Z+8位数字
			 */
			citizenInfo.setCitizenSerialNo("");
			if("3".equals(rcCardType)) {
				str = rs.getCell(14, i).getContents().trim().replace("\t", "");
				if(str==null || "".equals(str) || !str.matches("[zZ]\\d{8}")) {
					msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的市民卡编号["+str+"]不合规，无法导入数据库;<br/>");
					isInDatabase = false;
				}
				citizenInfo.setCitizenSerialNo(str);
			}
			
			/*
			 * 市民卡号
			 * 只有市民卡存在该字段，其余卡款允许为空，值为不超过20位数字
			 */
			citizenInfo.setCitizenNo("");
			if("3".equals(rcCardType)) {
				str = rs.getCell(15, i).getContents().trim().replace("\t", "");
				if(str==null || "".equals(str) || !str.matches("\\d{16,20}")) {
					msg.append("第"+(i+1)+"行，居住证号为["+rcCard+"]的市民卡号["+str+"]不合规，无法导入数据库;<br/>");
					isInDatabase = false;
				}
				citizenInfo.setCitizenNo(str);
			}
			
			//批次号
			citizenInfo.setBatchNo(serialNo);
			//主键
			citizenInfo.setId(serialNo+rcCard);
			
			//如果数据无问题则加入数据列表
			if(isInDatabase) {				
				citizenInfoList.add(citizenInfo);
			}
		}
		
		if(!isInDatabase) {
			resMap.put("resCode", "-1");
			resMap.put("resMessage", "流口数据存在问题，无法导入，请检查数据后重新导入");
			resMap.put("resMessage1", String.valueOf(msg));
			return resMap;
		}
				
		// 将流口信息集合存入Map返回
		if(citizenInfoList.size() == recordCount) {		
			resMap.put("citizenInfoList", citizenInfoList);
		} else {
			resMap.put("resCode", "-1");
			resMap.put("resMessage", "流口数据数量为："+citizenInfoList.size()+"与文件名数量字段"+recordCount+"不匹配，请确认！");
			return resMap;
		}
		
		logger_.debug("upload citizen data end");
		
		resMap.put("resCode", "0");
		return resMap;
	}

	/**
	 * 导入流口照片文件
	 * 
	 * @param phoFile
	 *            照片文件
	 * @param zipPhoFile
	 *            压缩后照片文件
	 * @param divisionsId
	 *            地区码
	 * @param serialNo
	 *            序列码
	 * @param phoRelaName
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	private Map<String, String> upPhoFile(File phoFile, String serialNo, String photoSavePath, List<CitizenInfoModel> tempCitizenList) throws Exception {
		logger_.debug("upload citizen photo start");
		CitizenInfoDao citizenInfoDao = this.sqlSession.getMapper(CitizenInfoDao.class);
		Map<String, String> resMap = new HashMap<String, String>();
		//定义导入数据库的数据集合
		List<CitizenInfoModel> importList = new ArrayList<CitizenInfoModel>();

		//照片数量
		int photoCount = 0;
		//创建Zip输入流
		InputStream in = new FileInputStream(phoFile);
		ZipInputStream zipInputStream = new ZipInputStream(in,Charset.forName("GBK"));
		ZipEntry zipEntry;
		try {
			//给ZIP文件赋值
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
				} else {
					//创建图片文件
					InputStream EntryStream = new ZipFile(phoFile).getInputStream(zipEntry);
					File imfile = new File(photoSavePath + File.separator + zipEntry.getName());
					if (!imfile.exists()) {
						imfile.createNewFile();
					}
					/*通过imageIO读取ZIP里的图片文件并对图片进行压缩，尺寸为71*99，写入到imfile中,有个别图片无法压缩会报错,
					 * 所以针对这种情况就需要直接把图片复制到指定目录不做压缩处理
					 * */
					BufferedInputStream bis = new BufferedInputStream(EntryStream);
					try {
						resize(bis,71,99,photoSavePath + File.separator + zipEntry.getName());
						EntryStream.close();
					} catch (Exception e) {
						EntryStream.close();
						bis.close();
						
						EntryStream = new ZipFile(phoFile).getInputStream(zipEntry);
						//读取ZIP里的图片文件，写入到imfile中
						FileOutputStream fileOut = new FileOutputStream(imfile);
						BufferedOutputStream bos = new BufferedOutputStream(fileOut);
						bis = new BufferedInputStream(EntryStream);
						int le = 0;
						byte[] b = new byte[1024*1024];
						while ((le = bis.read(b)) != -1) {
							bos.write(b,0,le);
						}
						bos.flush();						
						fileOut.close();			
						bos.close();
					}
					
					EntryStream.close();
					bis.close();
														
					//获取图片文件名
					String zipEntryName = zipEntry.getName();
					int indPoint = zipEntryName.indexOf(".");
					int indSprit = zipEntryName.indexOf("/");
					String phoIdNo =zipEntryName.substring(indSprit + 1, indPoint);
					//循环对比流口身份证号与图片名，如果一致则加入集合里
					for(CitizenInfoModel citInfo :tempCitizenList){
						String xlsIdcardNo = citInfo.getIdCard();
						if(xlsIdcardNo.equalsIgnoreCase(phoIdNo)){
							String photo64base = ImageUtils.getImageStr(imfile);
							citInfo.setPhotoUrl(serialNo + File.separator + zipEntryName);
							citInfo.setPhotoBase64(photo64base);
						
							//将photoInfo存入集合
							importList.add(citInfo);
							break;
						}
						//批量导入图片数据，每次导入300张照片，然后清除集合数据
						if(importList.size() >= 300) {
							citizenInfoDao.importCitizenData(importList);
							photoCount += importList.size();
							importList.clear();
						}
					}				
				}
			}
			
			//批量导入剩下不大于300条的图片数据，然后清除集合数据
			if(importList.size()>0) {
				citizenInfoDao.importCitizenData(importList);
				photoCount += importList.size();
				importList.clear();
			}
			
			in.close();
			//判断匹配成功的照片数量与流口数量是否一致，一致则存入临时表，否则报错
			if(photoCount != tempCitizenList.size()) {
				resMap.put("resCode", "-1");
				resMap.put("resMessage", "照片文件与流口数据不是一一匹配，请重新生成数据导入");
				logger_.debug("照片文件与流口数据不是一一匹配，请重新生成数据导入");
				return resMap;
			}
						
		} catch (Exception ex) {
			logger_.error("流口数据导入失败：" + ex.getMessage());
			resMap.put("resCode", "-1");
			resMap.put("resMessage", "数据库异常，流口数据导入失败。");
			return resMap;
		}
		resMap.put("resCode", "0");
		resMap.put("resMessage", "流口数据导入成功。");
		logger_.debug("upload citizen photo end");
		return resMap;
	}
	
	/**
	 * 压缩图片
	 * @param is
	 * @param w
	 * @param h
	 * @param outPath
	 * @throws IOException
	 */
    private void resize(InputStream is , int w, int h, String outPath) throws IOException {  
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
    	BufferedImage imagesrc = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );
    	BufferedImage image = ImageIO.read(is);  
    	Graphics g = imagesrc.getGraphics();
    	g.setColor(Color.white);
    	g.drawImage(image, 0, 0, w, h, null); // 绘制缩小后的图  
        File destFile = new File(outPath);  
        // 可以正常实现bmp、png、gif转jpg  
        //BufferedImage  ---->byte[]
        ImageIO.write(imagesrc,"jpg", destFile);     
    } 
	
	public static void main(String[] args) throws Exception {
				
//				System.out.println(DateUtils.getNow("HH:mm:ss"));
//				File phoFile = new File("F:\\helpdata\\BAK\\resAppData-330600(20161227-20161228)-29441-pictures.zip");
//				String serialNo = ResidenceUtils.createSerializeNO();
//				//创建Zip输入流
//				InputStream in = new FileInputStream(phoFile);
//				ZipInputStream zipInputStream = new ZipInputStream(in,Charset.forName("GBK"));
//				ZipEntry zipEntry;
//				FileWriter fw = new FileWriter("F:\\helpdata\\BAK\\"+"resAppData-330600(20161227-20161228)-29441-pictures.sql",true);
//				BufferedWriter bw = new BufferedWriter(fw);  
//				bw.write("SET autocommit = off;");
//				bw.newLine();	
//				bw.flush();
//				int count = 0;
//				//给ZIP文件赋值
//				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//					
//						//创建图片文件
//						InputStream EntryStream = new ZipFile(phoFile).getInputStream(zipEntry);
//						File imfile = new File("F:\\helpdata\\BAK\\1\\"+ zipEntry.getName() );
//						if (!imfile.exists()) {
//							imfile.createNewFile();
//						}
//						//读取ZIP里的图片文件，写入到imfile中
//						BufferedInputStream bis = new BufferedInputStream(EntryStream);
//						try {							
//							resize(bis,71,99,"F:\\helpdata\\BAK\\1\\"+ zipEntry.getName());					
//						} catch (Exception e) {
//							EntryStream.close();
//							bis.close();
//							
//							System.out.println(zipEntry.getName());
//							EntryStream = new ZipFile(phoFile).getInputStream(zipEntry);
//							//读取ZIP里的图片文件，写入到imfile中
//							FileOutputStream fileOut = new FileOutputStream(imfile);
//							BufferedOutputStream bos = new BufferedOutputStream(fileOut);
//							bis = new BufferedInputStream(EntryStream);
//							int le = 0;
//							byte[] b = new byte[1024*1024];
//							while ((le = bis.read(b)) != -1) {
//								bos.write(b,0,le);
//							}
//							bos.flush();
//													
//							fileOut.close();			
//							bos.close();
//						}
//						EntryStream.close();
//						bis.close();
//
//						// 封装Model
//						CitizenPhotoInfoModel photoInfo = new CitizenPhotoInfoModel();
//						
//						//获取图片文件名
//						String zipEntryName = zipEntry.getName();							
//						int indPoint = zipEntryName.indexOf(".");
//						int indSprit = zipEntryName.indexOf("/");
//						String phoIdNo =zipEntryName.substring(indSprit + 1, indPoint);
//						//循环对比流口身份证号与图片名，如果一致则加入集合里
//						
//						photoInfo.setId(serialNo+phoIdNo);
//						photoInfo.setIdcardNo(phoIdNo);
//						photoInfo.setPhotoUrl(serialNo + File.separator + zipEntryName);
//						String photo64base = ImageUtils.getImageStr(imfile);
//						photoInfo.setPhotoBase64(photo64base);
//						photoInfo.setBatchNo(serialNo);
//
//						//
//						
//						
//						String sql = "UPDATE rc_card_info SET rc_head='"+photo64base+"' WHERE id_card='"+phoIdNo+"';";
//						bw.write(sql);
//						bw.newLine();	
//						count +=1;
//						if(count>=200) {
//							bw.write("commit;");
//							count=0;
//						}
//						bw.newLine();	
//						fw.flush();
//						bw.flush();
//						
//					
//				}
//				bw.write("commit;");
//				bw.newLine();
//				bw.write("SET autocommit = ON;");
//				bw.flush();
//				System.out.println(DateUtils.getNow("HH:mm:ss"));
//				in.close();
//				fw.close();
//				bw.close();
					
	}

}
