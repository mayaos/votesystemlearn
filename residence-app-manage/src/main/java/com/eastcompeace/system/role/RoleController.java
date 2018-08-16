package com.eastcompeace.system.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.UserUtil;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.RoleButtonModel;
import com.eastcompeace.model.RoleMappingModel;
import com.eastcompeace.model.RoleModel;
import com.eastcompeace.model.UserCommModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.model.UserRolesModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.system.user.UserCommDao;
import com.eastcompeace.system.user.UserDao;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.IntegerUtils;
import com.eastcompeace.util.BuildModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 角色管理类（角色增、删、改、查功能）
 * 
 * @author caobo
 * 
 */
@Controller
@RequestMapping("/rolesctrl")
public class RoleController extends BaseController {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 角色管理基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("common/role/");
	}

	/**
	 * 查询分页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getlist")
	@ResponseBody
	public JSONObject getList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleDao roleDao = sqlSession.getMapper(RoleDao.class);
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String strRoleName = request.getParameter("roleName");
		
		
		UserCommDao userCommDao = sqlSession.getMapper(UserCommDao.class);
		UserModel um = UserUtil.getUser(request);
		UserCommModel userComm = userCommDao.queryByUser(um.getUserId()+"");
		if(userComm == null ){
			return jsonObj;
		}
		if("2".equals(userComm.getType())){
			return jsonObj;
		}
		
		RoleModel role = new RoleModel();
		role.setRole_name(strRoleName);
		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<RoleModel> list = roleDao.queryForList(role);
		PageInfo<RoleModel> pages = new PageInfo(list);
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", list);
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel( "Y", "查询角色树", String.valueOf(Logenum.QUERYLIST), request));
		/**
		 * 日志end
		 */
		return jsonObj;
	}

	/**
	 * 根据条件更新
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update")
	@ResponseBody
	public JSONObject update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleDao roleDao = sqlSession.getMapper(RoleDao.class);
		JSONObject jsonObj = new JSONObject();
		boolean flag = true;
		String msg = null;
		String intId = request.getParameter("id");
		String strRoleName = request.getParameter("roleName");
		String strRoleDesc = request.getParameter("roleDesc");
		String isdefault = request.getParameter("isdefault");
		if (null == intId || "" == intId) {
			msg = "更新失败";
			flag = false;
			jsonObj.put("result", flag);
			jsonObj.put("message", msg);
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "更新角色", String.valueOf(Logenum.EDITINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}
		//检查角色名是否重复
		RoleModel role = new RoleModel();
		role.setRole_name(strRoleName);
		try {
			List<RoleModel> list = roleDao.query(role);
			if (null != list && list.size() > 0) {
				flag = false;
				msg = "已存在该角色名";
				jsonObj.put("result", flag);
				jsonObj.put("message", msg);
				return jsonObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			msg = "系统错误";
			jsonObj.put("result", flag);
			jsonObj.put("message", msg);
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "修改角色", String.valueOf(Logenum.EDITINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}
		
		role.setRole_desc(strRoleDesc);
		role.setRole_id(Integer.parseInt(intId));
		role.setIsdefault(isdefault);
		try {
			roleDao.update(role);
			msg = "更新成功";
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "更新角色", String.valueOf(Logenum.EDITINFO), request));
			/**
			 * 日志end
			 */
		} catch (Exception e) {
			e.printStackTrace();
			msg = "更新失败";
			flag = false;
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "更新角色", String.valueOf(Logenum.EDITINFO), request));
			/**
			 * 日志end
			 */
		}
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
	}

	/**
	 * 插入
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public JSONObject insert(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleDao roleDao = sqlSession.getMapper(RoleDao.class);
		JSONObject jsonObj = new JSONObject();
		boolean flag = true;
		String msg = null;
		String strRoleName = request.getParameter("roleName");
		String strRoleDesc = request.getParameter("roleDesc");
		String isdefault = request.getParameter("isdefault");
		RoleModel role = new RoleModel();
		role.setRole_name(strRoleName);
		try {
			List<RoleModel> list = roleDao.query(role);
			if (null != list && list.size() > 0) {
				flag = false;
				msg = "已存在该角色名";
				jsonObj.put("result", flag);
				jsonObj.put("message", msg);
				return jsonObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			msg = "系统错误";
			jsonObj.put("result", flag);
			jsonObj.put("message", msg);
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "新增角色", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}
		role.setRole_desc(strRoleDesc);
		role.setCreate_date(DateUtils.getNow());
		role.setIsdefault(isdefault);
		try {
			int i = roleDao.insert(role);
			msg = "插入成功";
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "新增角色", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			msg = "插入失败";
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "新增角色", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
		}
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JSONObject dele(HttpServletRequest request,HttpServletResponse response) throws Exception {
		RoleDao roleDao = sqlSession.getMapper(RoleDao.class);
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		JSONObject jsonObj = new JSONObject();
		boolean flag = true;
		String msg = null;
		String strids = request.getParameter("strids");
		String[] arrayid = strids.split(",");
		List<String> listid = new ArrayList<String>();
		for (int i = 0; i < arrayid.length; i++) {
			listid.add(arrayid[i]);
		}
		List<UserRolesModel> listUserRole = userDao.queryUserroleslistByroleId(listid);
		List<String> usedRoleId = new ArrayList<String>();
		// 判断roleId有没有被使用
		if (null != listUserRole && listUserRole.size() != 0) {
			for (int i = 0; i < listUserRole.size(); i++) {
				usedRoleId.add(listUserRole.get(i).getRoleId()+""); // 记录已经被使用了的roleId
			}
			listid.removeAll(usedRoleId); // 从原ID集合中删除被使用的roleid
			try {
				if(listid.isEmpty()){
					msg = "选中的角色都在使用，不能删除";
				}else {
					roleDao.delete(listid);
					roleDao.deleteRoleMapping(listid);
					roleDao.deleteRoleButton(listid);
					List<RoleModel> listRoleModel = roleDao.queryById(usedRoleId);
					msg = "其中：";
					if (null != listRoleModel && listRoleModel.size() != 0) {
						for (int i = 0; i < listRoleModel.size(); i++) {
							msg += listRoleModel.get(i).getRole_name() + ",";
						}
						msg = "删除成功" + msg + "已经被使用，不能删除";
					} else {
						msg = "删除成功";
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				msg = "删除失败";
				flag = false;
				/**
				 * 日志start
				 */
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "N", "删除角色", String.valueOf(Logenum.DELETEINFO), request));
				/**
				 * 日志end
				 */
			}
		} else {
			try {
				roleDao.delete(listid);
				roleDao.deleteRoleMapping(listid);
				roleDao.deleteRoleButton(listid);
				msg = "删除成功";
				/**
				 * 日志start
				 */
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "Y", "删除角色", String.valueOf(Logenum.DELETEINFO), request));
				/**
				 * 日志end
				 */
			} catch (Exception e) {
				e.printStackTrace();
				msg = "删除失败";
				flag = false;
				/**
				 * 日志start
				 */
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "N", "删除角色", String.valueOf(Logenum.DELETEINFO), request));
				/**
				 * 日志end
				 */
			}
		}
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
	}

	/**
	 * 根据角色ID显示对应权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolemenu")
	@ResponseBody
	public JSONObject rolemenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		RoleMappingDao rd = sqlSession.getMapper(RoleMappingDao.class);
		String strRoleId = request.getParameter("strRoleId");
		RoleMappingModel rmm = new RoleMappingModel();
		rmm.setRole_id(strRoleId);
		List<RoleMappingModel> list = rd.queryForList(rmm);
		jsonObj.put("result", "0");
		jsonObj.put("message", "");
		jsonObj.put("rList", list);
		return jsonObj;
	}
	
	/**
	 * 根据角色ID显示对应权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolebutton")
	@ResponseBody
	public JSONObject rolebutton(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		RoleMappingDao rd = sqlSession.getMapper(RoleMappingDao.class);
		String strRoleId = request.getParameter("strRoleId");
		RoleMappingModel rmm = new RoleMappingModel();
		Map<String, String[]> map = new LinkedMap();
		rmm.setRole_id(strRoleId);
		List<RoleMappingModel> list = rd.queryForList(rmm);
		jsonObj.put("result", "0");
		for (int i = 0; i < list.size(); i++) {
			List<RoleButtonModel> listbutton = rd.getButtonList(strRoleId, list.get(i).getMenu_id());
			String[] buttonid = new String[listbutton.size()];
			for (int j = 0; j < listbutton.size(); j++) {
				buttonid[j] = listbutton.get(j).getButtonid()+"#"+list.get(i).getMenu_id();
			}
			map.put(list.get(i).getMenu_id(), buttonid);
		}
		jsonObj.put("map", map);
		return jsonObj;
	}

	
	
	/**
	 * 保存角色对应的权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/roleinsert")
	@ResponseBody
	public JSONObject roleinsert(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleMappingDao rd = sqlSession.getMapper(RoleMappingDao.class);
		JSONObject jsonObj = new JSONObject();
		String strRoleId = request.getParameter("strRoleId");
		String strMenuIds = request.getParameter("strmenuIds");
		List list = new ArrayList<>();
		if(null!=strMenuIds && ""!=strMenuIds.trim()){
			String[] idarry = strMenuIds.split(",");
			 list = Arrays.asList(idarry);
			if (null == strRoleId || "" == strRoleId) {
				jsonObj.put("message", "参数错误！");
				return jsonObj;
			}
		}

		List<RoleMappingModel> listr = new ArrayList<RoleMappingModel>();
		for (int i = 0; i < list.size(); i++) {
			RoleMappingModel rmm = new RoleMappingModel();
			rmm.setRole_id(strRoleId);
			rmm.setMenu_id(list.get(i) + "");
			listr.add(i, rmm);
		}
		rd.delete(Integer.parseInt(strRoleId));
		if(null!=listr && listr.size()>0){
			try {
				int i = rd.insert(listr);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObj.put("message", "程序内部错误！");
				/**
				 * 日志start
				 */
				LogDao logDao = sqlSession.getMapper(LogDao.class);
				logDao.add(BuildModel.getModel( "N", "添加权限", String.valueOf(Logenum.EMPOWER), request));
				/**
				 * 日志end
				 */
				return jsonObj;
			}
		}
		
		

		jsonObj.put("result", 0);
		jsonObj.put("message", "修改成功！");
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel( "Y", "添加权限", String.valueOf(Logenum.EMPOWER), request));
		/**
		 * 日志end
		 */
		return jsonObj;
	}
	
	/**
	 * 保存角色菜单对应的按钮
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolebuttoninsert")
	@ResponseBody
	public JSONObject rolebuttoninsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		RoleMappingDao rd = sqlSession.getMapper(RoleMappingDao.class);
		JSONObject jsonObj = new JSONObject();
		String strRoleId = request.getParameter("strRoleId");
		String strMenuIds = request.getParameter("strmenuIds");
		String[] idarry = strMenuIds.split(",");
		List list = Arrays.asList(idarry);
		if (null == strRoleId || "" == strRoleId) {
			jsonObj.put("message", "参数错误！");
			return jsonObj;
		}
		List<RoleButtonModel> listr = new ArrayList<RoleButtonModel>();
	    for (int i = 0; i < list.size(); i++) {
		   if((list.get(i)+"").indexOf("#")>0){
			   int k = (list.get(i)+"").indexOf("#");
			   RoleButtonModel rbm = new RoleButtonModel();
			   rbm.setRoleid(strRoleId);
			   rbm.setButtonid((list.get(i)+"").substring(0, k));
			   rbm.setMeunid((list.get(i)+"").substring(k+1, (list.get(i)+"").length()));
			   listr.add(rbm);
		   }
		}	
		try {
			rd.deletebutton(Integer.parseInt(strRoleId));
			if (listr.size()>0) {
				rd.insertbutton(listr);
			}else {
				jsonObj.put("result", 0);
				jsonObj.put("message", "修改成功！");
				return jsonObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObj.put("message", "程序内部错误！");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "添加按钮", String.valueOf(Logenum.EMBUTTON), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}

		jsonObj.put("result", 0);
		jsonObj.put("message", "修改成功！");
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel( "Y", "添加按钮", String.valueOf(Logenum.EMBUTTON), request));
		/**
		 * 日志end
		 */
		return jsonObj;
	}
	
	@RequestMapping("/querySelectList")
	@ResponseBody
	public JSONArray querySelectList(HttpServletRequest request,HttpServletResponse response) throws Exception {		
		RoleDao roleDao = sqlSession.getMapper(RoleDao.class);
		List <RoleModel> rowList = roleDao.querySelectList();
		JSONArray jsonArray = JSONArray.fromObject(rowList);
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