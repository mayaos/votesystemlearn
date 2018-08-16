package com.eastcompeace.system.codedict;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.CodedictModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.system.menu.MenuDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 业务码表（增、删、改、查）
 * @author suyang
 *
 */
@Controller
@RequestMapping("/codectrl")
public class CodedictController extends BaseController{
	
	@Resource
	private SqlSession sqlSession;

	/**
	 * 业务码表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/codedict/");
	}
	
	/**
	 * 查询业务码表列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/codelist")
	@ResponseBody
	public JSONObject getCodeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj=new JSONObject();
		CodedictDao codedictDao=sqlSession.getMapper(CodedictDao.class);
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		
		CodedictModel cm=new CodedictModel();
		cm.setCodeName(request.getParameter("codeName"));
		cm.setCodeType(request.getParameter("codeType"));
		cm.setCodeTypeName(request.getParameter("codeTypeName"));
		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CodedictModel> codelist = codedictDao.queryCodeList(cm);
		PageInfo<CodedictModel> pages = new PageInfo(codelist);

		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows",codelist);
		return jsonObj;
	}
	/**
	 * 添加业务码表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addCodedictInfo")
	@ResponseBody
	public JSONObject addCodeInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj=new JSONObject();
		CodedictDao codedictDao=sqlSession.getMapper(CodedictDao.class);
		CodedictModel cm=new CodedictModel();
		
		cm.setCodeDate(request.getParameter("codeDate").trim());
		cm.setCodeName(request.getParameter("codeName").trim());
		cm.setCodeOrder(request.getParameter("codeOrder").trim());
		cm.setCodeType(request.getParameter("codeType").trim());
		cm.setCodeTypeName(request.getParameter("codeTypeName").trim());
		cm.setCodeValue(request.getParameter("codeValue").trim());
		cm.setCodeValid(request.getParameter("codeValid").trim());
		
		/*
		 * 检查codeType，code_value是否能查到数据，存在则返回错误，
		 * codeType，code_value组合在数据库中是唯一的
		 */
		int count = 0;
		try {
			count = codedictDao.queryCodedict(cm);
		} catch (Exception e) {
			jsonObj.put("result","1");
			jsonObj.put("message","访问数据库失败"+e.getMessage());
			return jsonObj;
		}
		if(count!=0) {
			jsonObj.put("result","1");
			jsonObj.put("message","码值类型和码值与现有数据冲突，请重新输入");
			return jsonObj;
		}
		
		try{
			codedictDao.insertCodeInfo(cm);
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","添加失败"+e.getMessage());
			return jsonObj;
		}
		jsonObj.put("result","0");
		jsonObj.put("message","添加成功");
		return jsonObj;
	}

	/**
	 * 修改业务码表
	 * 
	 * @param request
	 * @param response
	 *  @return
	 *  @throws Exception
	 */
	@RequestMapping("/updateCodedictInfo")
	@ResponseBody
	public JSONObject updateCodeInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj=new JSONObject();
		CodedictDao codedictDao=sqlSession.getMapper(CodedictDao.class);
		CodedictModel cm=new CodedictModel();
		
		cm.setCodeDate(request.getParameter("codeDate").trim());
		cm.setCodeName(request.getParameter("codeName").trim());
		cm.setCodeOrder(request.getParameter("codeOrder").trim());
		cm.setCodeType(request.getParameter("codeType").trim());
		cm.setCodeTypeName(request.getParameter("codeTypeName").trim());
		cm.setCodeValue(request.getParameter("codeValue").trim());
		cm.setCodeValid(request.getParameter("codeValid").trim());
		
		//访问数据库更新数据信息
		try{
			codedictDao.updateCodeInfo(cm);
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","修改失败"+e.getMessage());
			return jsonObj;
		}
		jsonObj.put("result","0");
		jsonObj.put("message","修改成功");
		return jsonObj;
	}
	
	/**
	 * 删除业务码表记录
	 * 
	 * @param request
	 * @param response
	 *  @return
	 *  @throws Exception
	 */
	@RequestMapping("/deleteCodedict")
	@ResponseBody
	public JSONObject deleteCodedict(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj=new JSONObject();
		CodedictDao codedictDao=sqlSession.getMapper(CodedictDao.class);
		
		
		String cType=request.getParameter("codeType");
		String cName=request.getParameter("codeName");
		String cValue=request.getParameter("codeValue");
		
		
		try{
			codedictDao.deleteCodedictInfo(cType,cName,cValue);
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","删除记录失败"+e.getMessage());
			return jsonObj;
		}
		jsonObj.put("result","0");
		jsonObj.put("message","删除记录成功");
		return jsonObj;
	}
	
	/**
	 * 根据字典类型查找字典名称和字典值
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createComboBox")
	@ResponseBody
	public JSONArray createComboBox(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		CodedictDao codedictDao = sqlSession.getMapper(CodedictDao.class);
		String codeType = request.getParameter("codeType");
		
		List<CodedictModel> codelist = codedictDao.selectCodelist(codeType);
		JSONArray jsonArray = JSONArray.fromObject(codelist);
		return jsonArray;
	}

	@RequestMapping("/relationSelect")
	@ResponseBody
	public JSONArray relationSelect(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String codeType = request.getParameter("codeType");
		String relationType = request.getParameter("relationType");
		
		CodedictDao codedictDao = sqlSession.getMapper(CodedictDao.class);
		
		List<CodedictModel> codelist = codedictDao.relationSelectList(codeType, relationType);
		JSONArray jsonArray = JSONArray.fromObject(codelist);
		return jsonArray;
	}
	
	/**
	 * 根据权限生成toolbar
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getToolbar")
	@ResponseBody
	public JSONObject getToolbar(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
		List<ButtonModel>  listbutton = md.queryButtonListby(roleid, menuid);
		if(null != listbutton && listbutton.size() > 0){
			jsonObj.put("result", listbutton);
		}
		return jsonObj;
	}

}
