package com.eastcompeace.system.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;

/**
 * 菜单管理类（菜单初始化、增、删、改）
 * 
 * @author caobo
 * 
 */
@Controller
@RequestMapping("/menuctrl")
public class MenuController extends BaseController {
	
	private Logger logger = Logger.getLogger("MenuController");
	@Resource
	private SqlSession sqlSession;

	/**
	 * 基础页面跳转 跳转至菜单管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("common/menu/");
	}

	/**
	 * 查询menu列表，用于首页生成左侧菜单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/menulist")
	@ResponseBody
	public JSONObject selectMenulist(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		List<MenuModel> menuidlist = (List<MenuModel>) session
				.getAttribute(Constant.SESSION_POWER);
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		List<MenuModel> menulist = menuDao.queryMenuList(menuidlist);
		List<MenuModel> faMenulist = new ArrayList<MenuModel>();
		List<MenuModel> chMenulist = new ArrayList<MenuModel>();
		//处理未赋权限用户登录时，菜单显示问题
		if(menuidlist==null){
			jsonObj.put("result", 0);
			jsonObj.put("message", "菜单获取失败，用户暂未分配访问权限,请在分配权限后重试。");
			return jsonObj;
		}
		for (MenuModel meobj : menulist) {
			if (meobj.getMenuFatherid() == 0) {
				faMenulist.add(meobj);
			} else {
				chMenulist.add(meobj);
			}
		}
		JSONArray ja = new JSONArray();
		for (MenuModel fmenuObj : faMenulist) {
			JSONObject json = new JSONObject();
			JSONArray ja2 = new JSONArray();
			for (MenuModel cmenuObj : chMenulist) {
				if (cmenuObj.getMenuFatherid() == fmenuObj.getMenuId()) {
					JSONObject json2 = new JSONObject();
					json2.put("id", cmenuObj.getMenuId());
					json2.put("text", cmenuObj.getMenuName());
					json2.put("url", cmenuObj.getMenuUrl());
					ja2.add(json2);
				}
			}
			json.put("id", fmenuObj.getMenuId());
			json.put("text", fmenuObj.getMenuName());
			json.put("url", fmenuObj.getMenuUrl());
			json.put("iconCls", fmenuObj.getMenuIcon());
			json.put("children", ja2);
			ja.add(json);
		}

		jsonObj.put("result", 0);
		jsonObj.put("message", "菜单获取成功");
		jsonObj.put("menuList", ja);
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		try {
			logDao.add(BuildModel.getModel( "Y", "查询菜单树", String.valueOf(Logenum.QUERYLIST), request));
		} catch (Exception e) {
			logger.error("日志存储失败", e);
		}
		/**
		 * 日志end
		 */
		return jsonObj;
	}

	/**
	 * 查询menu列表，用于首页生成菜单树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/menustree")
	@ResponseBody
	public JSONArray getMenutrees(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		List<MenuModel> menulist = menuDao.queryMenuListByFid(0);
		JSONArray ja = new JSONArray();
		for (Iterator<MenuModel> it = menulist.iterator(); it.hasNext();) {
			JSONObject json = new JSONObject();
			MenuModel mm = it.next();
			int menuId = mm.getMenuId();
			List<MenuModel> lstmenu2 = menuDao.queryMenuListByFid(menuId);
			JSONArray ja2 = new JSONArray();
			for (Iterator<MenuModel> it2 = lstmenu2.iterator(); it2.hasNext();) {
				MenuModel mm2 = it2.next();
				JSONObject json2 = new JSONObject();
				json2.put("id", mm2.getMenuId());
				json2.put("text", mm2.getMenuName());
				json2.put("url", mm2.getMenuUrl());
				ja2.add(json2);
			}
			json.put("id", mm.getMenuId());
			json.put("text", mm.getMenuName());
			json.put("url", mm.getMenuUrl());
			json.put("iconCls", mm.getMenuIcon());
			json.put("children", ja2);
			ja.add(json);
		}
		JSONArray jsarry = new JSONArray();
		jsonObj.put("id", 0);
		jsonObj.put("text", "菜单列表");
		jsonObj.put("url", "");
		jsonObj.put("iconCls", "icon-folder");
		jsonObj.put("children", ja);
		jsarry.add(jsonObj);
		return jsarry;
	}
	
	/**
	 * 查询菜单对应的按钮
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/buttontree")
	@ResponseBody
	public JSONArray getButtontree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strRoleId = request.getParameter("strRoleId");
		JSONObject jsonObj = new JSONObject();
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		List<MenuModel> menulist = menuDao.queryMenuListByFid(0);
		JSONArray ja = new JSONArray();
		for (Iterator<MenuModel> it = menulist.iterator(); it.hasNext();) {
			JSONObject json = new JSONObject();
			MenuModel mm = it.next();
			int menuId = mm.getMenuId();
			List<MenuModel> lstmenu2 = menuDao.queryMenuListByFidroleid(menuId,strRoleId);
			JSONArray ja2 = new JSONArray();
			for (Iterator<MenuModel> it2 = lstmenu2.iterator(); it2.hasNext();) {
				MenuModel mm2 = it2.next();
				List<MenuButtonModel> button = menuDao.queryButtonListByFid(mm2.getMenuId());
				JSONArray ja3 = new JSONArray();
				for (Iterator<MenuButtonModel> it3 = button.iterator(); it3.hasNext();) {
					MenuButtonModel mbm = it3.next();
					JSONObject json3 = new JSONObject();
					json3.put("id", mbm.getButtonid()+"#"+mbm.getMenuid());
					json3.put("text", mbm.getButtontext());
					json3.put("fatherid", mbm.getMenuid());
					ja3.add(json3);
				}
				JSONObject json2 = new JSONObject();
				json2.put("id", mm2.getMenuId());
				json2.put("text", mm2.getMenuName());
				json2.put("url", mm2.getMenuUrl());
				json2.put("children", ja3);
				ja2.add(json2);
			}
			json.put("id", mm.getMenuId());
			json.put("text", mm.getMenuName());
			json.put("url", mm.getMenuUrl());
			json.put("iconCls", mm.getMenuIcon());
			json.put("children", ja2);
			ja.add(json);
		}
		JSONArray jsarry = new JSONArray();
		jsonObj.put("id", 0);
		jsonObj.put("text", "菜单列表");
		jsonObj.put("url", "");
		jsonObj.put("iconCls", "icon-folder");
		jsonObj.put("children", ja);
		jsarry.add(jsonObj);
		return jsarry;
	}

	/**
	 * 根据menu_id 获取菜单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectmenuinfo")
	@ResponseBody
	public JSONObject getMenuModelInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		String menuId = request.getParameter("se_menu_id");
		MenuModel menu = menuDao.findMenuById(menuId);
		if (menu != null) {
			jsonObj.put("result", "0");
			jsonObj.put("message", "");
			jsonObj.put("menuObj", menu);
		} else {
			jsonObj.put("result", "1");
			jsonObj.put("message", "菜单对象查询失败");
		}

		return jsonObj;
	}

	/**
	 * 根据menu_id 删除菜单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delmenuinfo")
	@ResponseBody
	public JSONObject delMenuModelInfo(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		String menuId = request.getParameter("del_menu_id");

		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);

//		MenuModel menuObj = menuDao.findMenuById(menuId);
//		String isleaf = menuObj.getMenuIsleaf();
//		if (isleaf.equals(Constant.MENU_ISLEAF)) {
//			jsonObj.put("result", "2");
//			jsonObj.put("message", "菜单不是子节点，不可删除");
//			return jsonObj;
//		}
		//判断menuId是否为数字字符串
		if(menuId==null || !menuId.matches("[0-9]{1,11}")) {
			jsonObj.put("result", "1");
			jsonObj.put("message", "menuId不是数字字符串");
			return jsonObj;
		}
		
		//判断要删除的菜单是否有子菜单，若有则返回提示需要先删除完子菜单才能删除父菜单。
		List<MenuModel> list = null;
		try {
			list = menuDao.queryMenuListByFid(Integer.parseInt(menuId));
		} catch (Exception e) {
			e.printStackTrace();
			jsonObj.put("result", "1");
			jsonObj.put("message", "访问数据库失败");			
			//日志start			
			try {
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "N", "删除菜单", String.valueOf(Logenum.DELETEINFO), request));
			} catch (Exception e1) {
				logger.error("日志存储失败", e1);
			}
			// 日志end
			return jsonObj;
		}
		if(!list.isEmpty()) {
			jsonObj.put("result", "1");
			jsonObj.put("message", "该菜单存在子菜单，不能删除");				
			//日志start			
			try {
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "N", "删除菜单", String.valueOf(Logenum.DELETEINFO), request));
			} catch (Exception e1) {
				logger.error("日志存储失败", e1);
			}
			// 日志end
			return jsonObj;
		}

		try {
			// 第一步，删除角色菜单映射表中的数据
			menuDao.delRoleMappingByMid(menuId);
			// 第二步，删除菜单表中数据
			menuDao.delMenuById(menuId);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", "1");
			jsonObj.put("message", "数据库异常，删除菜单失败");				
			try {
				//存储操作记录到数据库
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "N", "删除菜单", String.valueOf(Logenum.DELETEINFO), request));
			} catch (Exception e1) {
				logger.error("日志存储失败", e1);
			}
			return jsonObj;
		}
		jsonObj.put("result", "0");
		jsonObj.put("message", "删除菜单成功,建议重新登录系统，更新菜单列表");		
		try {
			//存储操作记录到数据库
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "删除菜单", String.valueOf(Logenum.DELETEINFO), request));
		} catch (Exception e1) {
			logger.error("日志存储失败", e1);
		}
		return jsonObj;
	}

	/**
	 * 添加菜单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addmenuinfo")
	@ResponseBody
	public JSONObject addMenuModelInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		// 获取参数值（新建菜单名，父类菜单ID）
		String meName = request.getParameter("ad_menu_name");
		String meFatherId = request.getParameter("ad_menu_fatherid");

		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		try {
			menuDao.addMenuInfo(meName, meFatherId);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", "1");
			jsonObj.put("message", "添加菜单失败");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "添加菜单树", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}
		jsonObj.put("result", "0");
		jsonObj.put("message", "添加菜单成功");
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel( "N", "添加菜单树", String.valueOf(Logenum.ADDINFO), request));
		/**
		 * 日志end
		 */
		return jsonObj;
	}

	/**
	 * 更新menu信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updatemenuinfo")
	@ResponseBody
	public JSONObject updateMenuModelInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		// 获取dao对象
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		// 从页面获取数值
		String strButtonId = request.getParameter("buttonid");
		
		String[] id = strButtonId.split(",");
		String meId = request.getParameter("up_menu_id");
		String meName = request.getParameter("up_menu_name");
		String meIcon = request.getParameter("up_menu_icon");
		String meOrder = request.getParameter("up_menu_order");
		String meUrl = request.getParameter("up_menu_url");
		String meDesc = request.getParameter("up_menu_desc");
		MenuModel nMenu = new MenuModel();
		
		if (null == strButtonId || "".equals(strButtonId.trim())) {
			//菜单对应的所有按钮
			MenuButtonModel mbm = new MenuButtonModel();
			if (StringUtils.isNotBlank(meId)) {
				int menuid = Integer.parseInt(meId);
				menuDao.deleteMenuButton(menuid);
			}
			
			//修改菜单的其他信息
			nMenu.setMenuId(Integer.parseInt(meId));
			nMenu.setMenuName(meName);
			nMenu.setMenuIcon(meIcon);
			if (StringUtils.isNotBlank(meOrder)) {
				nMenu.setMenuOrder(Integer.parseInt(meOrder));
			}
			nMenu.setMenuDesc(meDesc);
			nMenu.setMenuUrl(meUrl);
			// 调用DAO接口中方法 ，更新menu表中数据
			try {
				menuDao.updateMenuInfo(nMenu);
			} catch (Exception ex) {
				ex.printStackTrace();
				jsonObj.put("result", "1");
				jsonObj.put("message", "数据库异常，更新数据失败");
				return jsonObj;
			}
			
			jsonObj.put("result", "0");
			jsonObj.put("message", "更新数据成功");
			return jsonObj;
		}
		
		//先删除关系表中所有与menueid有关的信息，再添加新的信息
		// 新建menuModel 对象，并赋值
		MenuButtonModel mbm = new MenuButtonModel();
		try {
			if (StringUtils.isNotBlank(meId)) {
				Integer menuid = Integer.parseInt(meId);
				//删除common_menubutton_mapping表中的所有与menuid有关的信息
				menuDao.deleteMenuButton(menuid);
				//删除common_rolebutton_mapping表中的所有与menueid有关的信息
				menuDao.delRoleButtonMapping(menuid);
				nMenu.setMenuId(Integer.parseInt(meId));
				mbm.setMenuid(menuid+"");
				for (int i = 0; i < id.length; i++) {
					mbm.setButtonid(id[i].toString());
					menuDao.addMenuButton(mbm);
				}		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", "1");
			jsonObj.put("message", "数据库异常，更新数据失败");
			return jsonObj;
		}
	
		nMenu.setMenuName(meName);
		nMenu.setMenuIcon(meIcon);
		if (StringUtils.isNotBlank(meOrder)) {
			nMenu.setMenuOrder(Integer.parseInt(meOrder));
		}
		nMenu.setMenuDesc(meDesc);
		nMenu.setMenuUrl(meUrl);
		// 调用DAO接口中方法 ，更新menu表中数据
		try {
			menuDao.updateMenuInfo(nMenu);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", "1");
			jsonObj.put("message", "数据库异常，更新数据失败");
			return jsonObj;
		}
		jsonObj.put("result", "0");
		jsonObj.put("message", "更新数据成功");
		return jsonObj;
	}

	/**
	 * 添加菜单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getbutton")
	@ResponseBody
	public JSONArray getbutton(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONArray jsArray = new JSONArray();
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		List<ButtonModel> list = menuDao.queryButtonList();
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", list.get(i).getUuid());
			jsonObj.put("text", list.get(i).getText());
			jsArray.add(jsonObj);
		}
		return jsArray;
	}
	
	/**
	 * 添加菜单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getbuttonby")
	@ResponseBody
	public JSONObject getbuttonbymeunID(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String menuId = request.getParameter("menuid");
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		List<MenuButtonModel> list = menuDao.queryButtonListByFid(Integer.parseInt(menuId));
		String[] str = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			str[i] = list.get(i).getButtonid();
		}
		jsonObject.put("str", str);
		return jsonObject;
	}

}