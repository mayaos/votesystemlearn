package com.eastcompeace.base;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.ResourceUtils;

/**
 * Controller基类 
 * 1 根据传入的模块路径，获取相对根路径url
 * 
 * @author caobo 20151022
 */
@Controller
public class BaseController {
	private Logger logger = Logger.getLogger("BaseController");
	/**
	 * 根据传入的模块路径，获取相对根路径url
	 * @param url 传入路径
	 * @return
	 */
	public ModelAndView getModelView(String url) {
		if (url.indexOf(".") > 0) {
			// no codes
		} else {
			if (!url.endsWith("/")) url += "/";
		}
		
		return new ModelAndView("../modules/" + url);
	}
	
	/**
	 * 动态加载工具
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public JSONObject getToolbar(HttpServletRequest request, SqlSession sqlSession) throws Exception {
		JSONObject jsonObj = new JSONObject();
		MenuDao md = sqlSession.getMapper(MenuDao.class);
		MenuModel mm = new MenuModel();
		String uri = request.getRequestURI();
		int index = 0;
		String str = uri;
		index = str.lastIndexOf("/");
		str = str.substring(0, index);
		index = str.lastIndexOf("/");
		str = str.substring(index + 1, str.length());
		String menuUrl = str + "/showindex";
		mm.setMenuUrl(menuUrl);
		String menuid = "";
		List<MenuModel> listmenu = md.queryMenuby(mm);
		if (null != listmenu && listmenu.size() > 0) {
			menuid = listmenu.get(0).getMenuId() + "";
		}
		String roleid = request.getSession().getAttribute(Constant.SESSION_ROLEID).toString();
		List<ButtonModel> listbutton = md.queryButtonListby(roleid, menuid);
		if(null != listbutton && listbutton.size() > 0){
			jsonObj.put("result", listbutton);
		}
		return jsonObj;
	}
	
	/**
	 * 上传图片到服务器
	 * @param strURL
	 * @param fis
	 * @param fieldName
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	protected String httpUpload(String strURL, InputStream fis, String fieldName, String fileName) throws Exception {
		final String boundaryPrefix = "--";
		final String boundary = "***d2a7fbc738cec380ee4ea0f1e54c07f8";
		final String newLine = "\r\n";
		byte[] data = new byte[0];
		

		URL url = new URL(strURL);
		
		// 设置http请求头参数
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		conn.connect();
		
		// 定义输出流对象
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		
		// 构建文件上传头参数
		StringBuffer sb = new StringBuffer();
		sb.append(boundaryPrefix + boundary + newLine);
		sb.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + newLine);
		sb.append("Content-Type: application/octet-stream" + newLine);
		sb.append("Content-Transfer-Encoding: binary" + newLine);
		sb.append(newLine);
		
		// 向文件服务器写入文件上传头参数
		dos.writeBytes(sb.toString());
		
		// 向文件服务器写入文件的字节流
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = fis.read(buffer)) != -1) {
			dos.write(buffer, 0, length);
		}
		
		// 文件字节流写入完成后必须加上换行
		dos.writeBytes(newLine);
		
		dos.flush();
		fis.close();
		
		// 向文件服务器写入baundary结束标记
		dos.writeBytes(boundaryPrefix + boundary + boundaryPrefix + newLine);
		
		// 从文件服务器取响应
		InputStream is = conn.getInputStream();
		byte[] buffer2 = new byte[1024];
		int length2 = 0;
		while ((length2 = is.read(buffer2)) != -1) {
			byte[] arrBytes = new byte[data.length + length2];
				
			System.arraycopy(data, 0, arrBytes, 0, data.length);
			System.arraycopy(buffer2, 0, arrBytes, data.length, length2);
			data = arrBytes;
		}
		
		// 关闭流
		dos.close();
		is.close();
		conn.disconnect();
		//将byte[] data转为字符串
		String jstr = new String(data, "UTF-8");
		if(jstr == null || "".equals(jstr)) {
			return "";
		}	
		JSONObject jObj = JSONObject.fromObject(jstr);
		if(!"0".equals(jObj.getString("result"))) {
			throw new Exception(jObj.getString("message"));
		}
		JSONObject jfileIDs = jObj.getJSONObject("fileIDs");
		
		//返回上传文件ID			
		return jfileIDs.getString(fieldName);
	
	}
	
	/**
	 * 上传图片文件到服务器
	 * 1)判断上传的图片文件是否为空，若为空则返回异常提示需要上传的图片
	 * 2)获取文件名进行格式对比，若pattern为空则所有图片格式都允许通过，若不为空
	 * 	 则与pattern进行对比，不一致抛异常
	 * 3)获取文件存储路径，存储在resources/configure.properties
	 * 4)调用httpUpload方法上传图片
	 * 5)判断oldImgId是否有值，若有值则对旧图片文件进行删除操作
	 * 6)返回上传图片的Id
	 * @param image
	 * @param oldImgId
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	protected String uploadImage(MultipartFile image,String oldImgId, String pattern) throws Exception  {
		//如果照片文件为空则返回
		if(image==null) {
			throw new Exception("图片文件为空，请选择需要上传的图片！");
		}	
		//获取图片文件名
		String imgFullName = image.getOriginalFilename();
		//判断图片格式，若pattern为空则.jpg/.png/.gif/.bmp都能通过，若不为空则匹配指定格式
		String type = imgFullName.substring(imgFullName.lastIndexOf("."));
		if(pattern!=null && !"".equals(pattern)) {
			if(!pattern.equalsIgnoreCase(type)) {
				throw new Exception("图片格式不对，请上传"+pattern+"格式图片！");
			}
		} else {
			if(!".jpg".equalsIgnoreCase(type) && !".png".equalsIgnoreCase(type) && !".gif".equalsIgnoreCase(type)
					&& !".bmp".equalsIgnoreCase(type)) {
				throw new Exception("上传的文件不是图片文件，请上传图片格式文件！");
			}
		}
					
		//获取文件存储路径，存储在resources/configure.properties
		//图片保存目录路径	
		String imgUrl = ResourceUtils.getProperty("httpUploadURL");
		
		//上传图片文件
		String imgId;
		try {
			imgId = httpUpload(imgUrl, image.getInputStream(), "img", image.getOriginalFilename());
		} catch (Exception e) {
			logger.error("上传图片文件失败:" + e.getMessage());
			throw new Exception("上传图片文件失败！");
		}
		
		//判断oldImgId是否存在，若存在则删除原始图片
		if(oldImgId!=null && !"".equals(oldImgId)) {
			try {
				deleteImg(oldImgId);
			} catch (Exception e) {
				logger.error("删除旧图片文件失败:" + e.getMessage());
				throw new Exception("删除旧图片文件失败！");
			}
		}
			
		//返回图片保存绝对路径
		return imgId;
	}
	
	/**
	 * 根据传的的图片ID删除服务器相应的图片
	 * @param imgId
	 */
	protected void deleteImg(String imgId) throws Exception{
		if(imgId==null || "".equals(imgId)) {
			return;
		}	
		String fileUrl = ResourceUtils.getProperty("httpDeleteURL")+imgId;
		byte[] data = new byte[0];
		//删除图片
		URL url = new URL(fileUrl); 
		HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
		httpUrlConnection.connect();
		InputStream is = httpUrlConnection.getInputStream();
		byte[] buffer2 = new byte[1024];
		int length2 = 0;
		while ((length2 = is.read(buffer2)) != -1) {
			byte[] arrBytes = new byte[data.length + length2];
				
			System.arraycopy(data, 0, arrBytes, 0, data.length);
			System.arraycopy(buffer2, 0, arrBytes, data.length, length2);
			data = arrBytes;
		}
		// 关闭流
		is.close();
		httpUrlConnection.disconnect();
		//将byte[] data转为字符串
		String jstr = new String(data, "UTF-8");
		if(jstr == null || "".equals(jstr)) {
			throw new Exception("删除图片失败!");
		}	
		JSONObject jObj = JSONObject.fromObject(jstr);	
		String result = jObj.getString("result");
		if(!"0".equals(result)) {
			throw new Exception(jObj.getString("message"));
		}	
	}
	
	public static void main(String[] args) throws Exception {
		String fileUrl = ResourceUtils.getProperty("httpDeleteURL")+"3bccdcb20a32423f9049e2897ec2aa7f";
		byte[] data = new byte[0];
		//删除图片
		URL url = new URL(fileUrl); 
		HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
		httpUrlConnection.connect();
		InputStream is = httpUrlConnection.getInputStream();
		byte[] buffer2 = new byte[1024];
		int length2 = 0;
		while ((length2 = is.read(buffer2)) != -1) {
			byte[] arrBytes = new byte[data.length + length2];
				
			System.arraycopy(data, 0, arrBytes, 0, data.length);
			System.arraycopy(buffer2, 0, arrBytes, data.length, length2);
			data = arrBytes;
		}
		
		// 关闭流
		is.close();
		//将byte[] data转为字符串
		String jstr = new String(data, "UTF-8");
		if(jstr == null || "".equals(jstr)) {
			return ;
		}	
		System.out.println("jstr:"+jstr);
//		JSONObject jObj = JSONObject.fromObject(jstr);
//		JSONObject jfileIDs = jObj.getJSONObject("fileIDs");
		httpUrlConnection.disconnect();
	}
}