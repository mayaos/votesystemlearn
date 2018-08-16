package com.eastcompeace.merchantApp.identification;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
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
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.MerchantAppModel;
import com.eastcompeace.model.OrgMenuModel;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.system.menu.MenuDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/merAppctrl")
public class MerAppIdentificationController extends BaseController{
	private Logger logger = Logger.getLogger("merAppIdentificationController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private MerAppIdentificationDao merAppIdentificationDao;
	/**
	 * 商家app
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView merAppIndex() {
		return super.getModelView("common/merApp/");
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
	 * 获取菜单列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectMerAppList")
	@ResponseBody
	@Transactional
	public JSONObject selectMerAppList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//实例化返回对象
		JSONObject josj = new JSONObject();
		//实例化列表信息
		List<MerchantAppModel> merAppList = null;
		//获取参数
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
			
		
		//查询数据库获取菜单列表
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			merAppList = merAppIdentificationDao.listMerchatApp();
			PageInfo<MerchantAppModel> pages = new PageInfo<MerchantAppModel>(merAppList);
			josj.put("total", pages.getTotal());
			josj.put("rows",merAppList);
		} catch (Exception e) {
			logger.error("查询失败:" + e.getMessage());
			return null;
		}	
		System.out.println(josj);
		return josj;
	}
	
    /**
     * 审批商家信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updateMerApp")
    @ResponseBody
    @Transactional
    public JSONObject updateMenu(HttpServletRequest request,
            HttpServletResponse response) {
        //实例化返回结果对象
        JSONObject jsos = new JSONObject();
        //实例化传入数据库的机构信息类
        MerchantAppModel model = new MerchantAppModel();
        // 建立多部分请求解析器
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
        //获取字符串参数
        String id = muRequest.getParameter("id");
        String step = muRequest.getParameter("step");
        String auditFailedMsg = muRequest.getParameter("auditFailedMsg");
        //给model成员变量赋值
        model.setId(id);
        model.setStep(step);
        model.setAuditFailedMsg(auditFailedMsg);
        //链接数据库添加机构菜单信息
        try {
            merAppIdentificationDao.updateMerApp(model);
        } catch (Exception e) {
            logger.error("数据库访问失败:" + e.getMessage());
            jsos.put("result","1");
            jsos.put("message","数据库异常，信息添加失败");
            return jsos;
        }
        
        jsos.put("result","0");
        jsos.put("message","信息更新成功");       
        
        return jsos;
    }
}
