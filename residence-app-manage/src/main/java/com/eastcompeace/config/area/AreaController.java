package com.eastcompeace.config.area;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.system.menu.MenuDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 区域管理类
 * 实现对区域信息的增、删、改、查功能
 * @author panyanlin
 *
 */
@Controller
@RequestMapping("/areactrl")
public class AreaController extends BaseController{
	
	private Logger logger = Logger.getLogger("AreaController");
	
	@Resource
	private SqlSession sqlSession;
	@Resource
	private AreaDao areaDao;
	/**
	 * 转发到area主页面
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView areaIndex() {
		return super.getModelView("config/area/");
	}
	/**
	 * 获取区域列表
	 * @return
	 */
	@RequestMapping("/areaList")
	@ResponseBody
	public JSONObject areaList(HttpServletRequest req) {
		//获取参数
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		//条件查询参数
		String areaNameSearch = req.getParameter("areaNameSearch");
		//定义变量
		List<AreaModel> areaModelList;
		PageInfo<AreaModel> pageinfo;
		JSONObject json = new JSONObject();
		//访问数据库获取区域信息列表
		try {
			PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
			areaModelList = areaDao.queryAreaList(areaNameSearch);
			pageinfo = new PageInfo<AreaModel>(areaModelList);
			json.put("total", pageinfo.getTotal());
			json.put("rows", areaModelList);
		} catch (Exception e) {
			logger.error("数据库异常，获取区域列表信息失败:" + e.getMessage());
			return null;
		}	
		return json;
	}
	
	/**
	 * 增加区域信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/addArea")
	@ResponseBody
	public JSONObject addArea(HttpServletRequest req) {
		//实例化结果类
		JSONObject jsonObj = new JSONObject();
		//获取参数
		String areaId = req.getParameter("areaId");
		String areaName = req.getParameter("areaName");
		String areaFullName = req.getParameter("areaFullName");
		//判断参数是否符合要求
		if(areaId==null || areaId.length()!=6 || !areaId.matches("[0-9]{6}")) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "areaId值不正确");
			logger.error("areaId值不正确");
			return jsonObj;
		}
		if(areaName==null || "".equals(areaName)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "areaName不能为空");
			logger.error("areaName不能为空");
			return jsonObj;
		}
		if(areaFullName==null || "".equals(areaFullName)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "areaFullName不能为空");
			logger.error("areaFullName不能为空");
			return jsonObj;
		}
		//省、城市、区县代码从areaId中截取
		String provinceCode = areaId.substring(0,2);
		String cityCode = areaId.substring(2, 4);
		String townCode = areaId.substring(4,6);
		//实例化区域类
		AreaModel areaModel = new AreaModel(areaId, areaName, areaFullName, provinceCode, cityCode, townCode);
		//访问数据库插入数据
		try {
			areaDao.addAreaInfo(areaModel);
		} catch (Exception e) {
			jsonObj.put("result", -1);
			jsonObj.put("message", "访问数据库插入区域信息失败");
			logger.error("访问数据库插入区域信息失败");
			return jsonObj;
		}
		jsonObj.put("result", 0);
		jsonObj.put("message", "增加区域信息成功");
		
		return jsonObj;
	}
	/**
	 * 修改区域信息（区域名，区域全名）
	 * @param req
	 * @return
	 */
	@RequestMapping("/updateArea")
	@ResponseBody
	public JSONObject updateArea(HttpServletRequest req) {
		//实例化结果类
		JSONObject jsonObj = new JSONObject();
		//获取参数
		String areaId = req.getParameter("areaId");
		String areaName = req.getParameter("areaName");
		String areaFullName = req.getParameter("areaFullName");
		//判断参数是否符合要求
		if(areaId==null || areaId.length()!=6 || !areaId.matches("[0-9]{6}")) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "areaId值不正确");
			logger.error("areaId值不正确");
			return jsonObj;
		}
		if(areaName==null || "".equals(areaName)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "areaName不能为空");
			logger.error("areaName不能为空");
			return jsonObj;
		}
		//省、城市、区县代码从areaId中截取
		String provinceCode = areaId.substring(0,2);
		String cityCode = areaId.substring(2, 4);
		String townCode = areaId.substring(4,6);
		//实例化区域类
		AreaModel areaModel = new AreaModel(areaId, areaName, areaFullName, provinceCode, cityCode, townCode);
		//访问数据库更新数据
		try {
			areaDao.updateAreaInfo(areaModel);
		} catch (Exception e) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "访问数据库更新区域信息失败");
			logger.error("访问数据库修改区域信息失败"+e.getMessage());
			return jsonObj;
		}
		jsonObj.put("result", 0);
		jsonObj.put("message", "修改区域信息成功");
		
		return jsonObj;
	}
	
	/**
	 * 根据区域ID删除区域信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/deleteArea")
	@ResponseBody
	public JSONObject deleteArea(HttpServletRequest req) {
		//实例化结果类
		JSONObject jsonObj = new JSONObject();
		//获取参数
		String areaId = req.getParameter("areaId");
		//判断参数是否符合要求
		if(areaId==null || areaId.length()!=6 || !areaId.matches("[0-9]{6}")) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "areaId值不正确");
			logger.error("areaId值不正确");
			return jsonObj;
		}
		try {
			areaDao.delAreaInfo(areaId);
		} catch (Exception e) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "访问数据库删除区域信息失败");
			logger.error("访问数据库删除区域信息失败"+e.getMessage());
			return jsonObj;
		}
		//组拼返回结果
		jsonObj.put("result", 0);
		jsonObj.put("message", "删除区域信息成功");
		
		return jsonObj;
	}
	/**
	 * 获取区域代码和地区全称，不分页
	 * @param req
	 * @return
	 */
	@RequestMapping("/areaFullNameList")
	@ResponseBody
	@Transactional
	public JSONArray areaFullNameList(HttpServletRequest req) {
		//定义数据库返回列表类
		List<Map<String,Object>> resultList = null;
		
		//访问数据库获取区域Id和区域全称
		try {
			resultList = areaDao.queryAreaIdAndFullNameList();
		} catch (Exception e) {	
			logger.error("访问数据库获取区域信息失败"+e.getMessage());
			return null;
		}
		
		JSONArray jsonArr = JSONArray.fromObject(resultList);
		
		return jsonArr;
	}
	
	/**
	 * 查询区域列表-下拉列表使用
	 * @param req: flag-1/2/3=省/市/区县
	 * @return
	 */
	@RequestMapping("/selectAreaList")
	@ResponseBody
	public JSONArray selectAreaList(HttpServletRequest req) {
		//获取参数
		String flag = req.getParameter("flag");
		String province = Constant.PROVINCE;
		String city = req.getParameter("city");

		//定义变量
		List<AreaModel> areaList = new ArrayList<AreaModel>();
		//访问数据库获取区域信息列表
		try {
			if("1".equals(flag)){
				areaList = areaDao.selectProvinceList();
			}else if("2".equals(flag)){
				areaList = areaDao.selectCityList(province);
			}else if("3".equals(flag)){
				if(city.length() == 2)
					areaList = areaDao.selectTownList(province, city);
				else{
					areaList = areaDao.selectTownList2(province, city);
				}
			}
		} catch (Exception e) {
			logger.error("数据库异常，获取区域列表信息失败:" + e.getMessage());
			return null;
		}	
		
		JSONArray jsonArray = JSONArray.fromObject(areaList);
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
	
	/**
	 * 地区树
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/addrTreelist")
	@ResponseBody
	public JSONArray getAddrTreeList(HttpServletRequest request, HttpServletResponse response) {
		JSONArray ja = new JSONArray();
		HttpSession session = request.getSession();
		
		UserModel userModel = (UserModel)session.getAttribute(Constant.SESSION_USER);
		
		
		// 获取省份信息
		try {
			if("admin".equals(userModel.getUserCode())||userModel==null||"330000".equals(userModel.getUserDivisionsId())) {
				ja = this.getProvince(); //全省信息
			} else {
				String userDivisionsId = userModel.getUserDivisionsId();
				ja = getTownByAreaId("33", userDivisionsId);
			}
			
		} catch (Exception e) {
			logger.error("数据库异常，获取区域列表信息失败:" + e.getMessage());
			return null;
		}
		return ja;
	}
	
	/**
	 * 获取省份信息
	 * @return
	 * @throws Exception 
	 */
	public JSONArray getProvince() throws Exception {	
		
		List<AreaModel> provinceList = areaDao.selectProvinceList();	
		JSONArray ja = new JSONArray();
		for (int i = 0; i < provinceList.size(); i++) {
			AreaModel province = provinceList.get(i);
			JSONObject json = new JSONObject();
			json.put("id", province.getAreaID());
			json.put("text", province.getAreaName());
			JSONArray ja1 = new JSONArray();
			ja1 = this.getCity(province.getAreaID().substring(0, 2));
			json.put("state", "closed");
			json.put("children", ja1);
			ja.add(json);
		}
		return ja;
	}

	/**
	 * 根据省份代码获取城市信息列表
	 * @param province 省份代码
	 * @return
	 * @throws Exception
	 */
	public JSONArray getCity(String province) throws Exception{
		List<AreaModel> citylist = areaDao.selectCityList(province);
		JSONArray ja = new JSONArray();
		for (int i = 0; i < citylist.size(); i++) {
			AreaModel city = citylist.get(i);
			JSONObject json = new JSONObject();
			json.put("id", city.getAreaID());
			json.put("text", city.getAreaName());
			JSONArray ja2 = new JSONArray();
			ja2 = getTown(city.getAreaID().substring(0, 2), city.getAreaID().substring(2, 4));
			json.put("state", "closed");
			json.put("children", ja2);
			ja.add(json);
		}
		return ja;
	}

	/**
	 * 根据省份和城市代码获取区域信息列表
	 * @param province
	 * @param city
	 * @return
	 * @throws Exception
	 */
	public JSONArray getTown(String province, String city) throws Exception {
		List<AreaModel> townlist = areaDao.selectTownList(province, city);
		JSONArray ja = new JSONArray();
		for (int i = 0; i < townlist.size(); i++) {
			AreaModel town = townlist.get(i);
			JSONObject json = new JSONObject();
			json.put("id", town.getAreaID());
			json.put("text", town.getAreaName());
			ja.add(json);
		}
		return ja;
	}
	
	/**
	 * 根据省份和城市代码获取区域信息列表
	 * @param province
	 * @param city
	 * @return
	 * @throws Exception
	 */
	public JSONArray getTownByAreaId(String province, String city) throws Exception {
		List<AreaModel> townlist = null;
		// 城市号码以"00"结尾的为市辖区 
		if("00".equals(city.substring(4, city.length()))) {
			townlist = areaDao.selectTownList2(province, city);
		} else {
			townlist = areaDao.selectTownList3(province, city);
		}
		 
		JSONArray ja = new JSONArray();
		for (int i = 0; i < townlist.size(); i++) {
			AreaModel town = townlist.get(i);
			JSONObject json = new JSONObject();
			json.put("id", town.getAreaID());
			json.put("text", town.getAreaName());
			ja.add(json);
		}
		return ja;
	}
}
