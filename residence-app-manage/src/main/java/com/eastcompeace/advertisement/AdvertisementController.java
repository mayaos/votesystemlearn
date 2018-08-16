package com.eastcompeace.advertisement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.UserUtil;
import com.eastcompeace.model.AdvInfoModel;
import com.eastcompeace.model.AdvMappingModel;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.RightsMenuModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.org.orginfo.OrgInfoDao;
import com.eastcompeace.rights.menu.RightsMenuDao;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Controller
@RequestMapping("/advctrl")
public class AdvertisementController extends BaseController {
    private Logger           logger = Logger.getLogger("AdvertisementController");
    @Resource
    private SqlSession       sqlSession;
    @Resource
    private AdvertisementDao advertisementDao;

    /**
     * 第三方机构跳转主页面
     * 
     * @return
     */
    @RequestMapping("/showindex")
    public ModelAndView orgInfoIndex() {
        return super.getModelView("adv/");
    }

    /**
     * 查询广告信息列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/querylist")
    @ResponseBody
    public JSONObject querylist(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        // 获取参数
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        // 条件查询参数
        String areaNameSearch = request.getParameter("areaNameSearch");
        // 访问数据库获取区域信息列表
        List<AdvInfoModel> advInfoList = null;
        PageInfo<AdvInfoModel> pageinfo = null;
        List<AdvInfoModel> tempList = new ArrayList<AdvInfoModel>();
        String imgUrl = ResourceUtils.getProperty("httpDownloadURL");
        try {
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
            advInfoList = advertisementDao.queryAdvInfoList(areaNameSearch);
            pageinfo = new PageInfo<AdvInfoModel>(advInfoList);
            if (advInfoList.isEmpty()) {
                json.put("total", pageinfo.getTotal());
                json.put("rows", tempList);
                return json;
            }
            // 获取广告发布区域名和区域ID，组成字符串赋值给对应的AdvInfoModel
            for (AdvInfoModel a : advInfoList) {
                List<AreaModel> areaList = advertisementDao.listAreaName(a.getId());
                StringBuffer areaNameBuff = new StringBuffer();
                StringBuffer areaIdBuff = new StringBuffer();
                if (!areaList.isEmpty()) {
                    for (AreaModel areaModel : areaList) {
                        areaNameBuff.append(areaModel.getAreaName());
                        areaNameBuff.append(",");
                        areaIdBuff.append(areaModel.getAreaID());
                        areaIdBuff.append(",");
                    }
                }
                if (areaNameBuff.length() > 0) {
                    // 除了末尾逗号，其余字符都转成字符串
                    a.setAreaNameList(areaNameBuff.substring(0, areaNameBuff.length() - 1).toString());
                    // 清空areaNameBuff内容
                    areaNameBuff.delete(0, areaNameBuff.length());
                }
                if (areaIdBuff.length() > 0) {
                    // 除了末尾逗号，其余字符都转成字符串
                    a.setAreaIdList(areaIdBuff.substring(0, areaIdBuff.length() - 1).toString());
                    // 清空areaIdBuff内容
                    areaIdBuff.delete(0, areaIdBuff.length());
                }
                // 赋值广告图片链接
                a.setAdvImgUrl(imgUrl + a.getUri());
                tempList.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据库异常，获取区域列表信息失败:" + e.getMessage());
            return null;
        }
        json.put("total", pageinfo.getTotal());
        json.put("rows", tempList);
        return json;
    }

    /**
     * 添加广告信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addadvinfo")
    @ResponseBody
    public JSONObject addAdvInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsos = new JSONObject();
        // 建立多部分请求解析器
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
        // 获取参数
        String[] areaIdList = muRequest.getParameterValues("areaId");
        MultipartFile advImg = muRequest.getFile("advImg");
        String advDesc = muRequest.getParameter("advDesc");
        String advValid = muRequest.getParameter("advValid");
        String redirectUrl = muRequest.getParameter("redirectUrl");
        // 获取上传管理员信息
        UserModel um = UserUtil.getUser(request);
        // 创建实例
        AdvInfoModel advInfoModel = new AdvInfoModel();
        advInfoModel.setAdvDesc(advDesc);
        advInfoModel.setRedirectUrl(redirectUrl);
        advInfoModel.setValid(advValid);
        advInfoModel.setCreator(um.getUserCode());
        try {
            advInfoModel.setUri(uploadImage(advImg, "", ""));
        } catch (Exception e) {
            logger.error("图片上传失败:" + e.getMessage());
            jsos.put("result", "1");
            jsos.put("message", "图片上传失败");
            return jsos;
        }
        // 访问数据库添加广告图片信息
        try {
            advertisementDao.saveAdvImgInfo(advInfoModel);
            advInfoModel = advertisementDao.getAdvImgInfo(advInfoModel);
            if (advInfoModel == null) {
                jsos.put("result", "1");
                jsos.put("message", "数据库异常请稍后再试");
                return jsos;
            }
        } catch (Exception e) {
            logger.error("数据库异常:" + e.getMessage());
            jsos.put("result", "1");
            jsos.put("message", "数据库异常请稍后再试");
            return jsos;
        }
        // 若用户没有选择发布区域，则直接返回信息保存成功
        if (areaIdList == null || areaIdList.length == 0) {
            jsos.put("result", "0");
            jsos.put("message", "信息保存成功");
            return jsos;
        }
        // 创建广告-区域关联对象实例
        try {
            List<AdvMappingModel> tempList = new ArrayList<AdvMappingModel>();
            for (String areaId : areaIdList) {
                AdvMappingModel advMappingModel = new AdvMappingModel();
                advMappingModel.setCreator(um.getUserCode());
                advMappingModel.setAreaId(areaId);
                advMappingModel.setImgId(advInfoModel.getId());
                tempList.add(advMappingModel);
                if (tempList.size() >= 200) {
                    advertisementDao.saveAdvMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                advertisementDao.saveAdvMapping(tempList);
            }
        } catch (Exception e) {
            logger.error("数据库异常:" + e.getMessage());
            jsos.put("result", "1");
            jsos.put("message", "数据库异常请稍后再试");
            return jsos;
        }
        jsos.put("result", "0");
        jsos.put("message", "信息保存成功");
        return jsos;
    }

    /**
     * 修改广告信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/editadvinfo")
    @ResponseBody
    public JSONObject editadvinfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsos = new JSONObject();
        // 建立多部分请求解析器
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
        // 获取参数
        String advId = muRequest.getParameter("advId1");
        String[] areaIdList = muRequest.getParameterValues("areaId1");
        String advDesc = muRequest.getParameter("advDesc1");
        String advValid = muRequest.getParameter("advValid1");
        String redirectUrl = muRequest.getParameter("redirectUrl1");
        // 获取上传管理员信息
        UserModel um = UserUtil.getUser(request);
        // 创建实例
        AdvInfoModel advInfoModel = new AdvInfoModel();
        advInfoModel.setId(advId);
        advInfoModel.setAdvDesc(advDesc);
        advInfoModel.setValid(advValid);
        advInfoModel.setCreator(um.getUserCode());
        advInfoModel.setRedirectUrl(redirectUrl);
        // 访问数据库添加广告图片信息
        try {
            advertisementDao.updateAdvImgInfo(advInfoModel);
            advertisementDao.delAdvMapping(advInfoModel);
        } catch (Exception e) {
            logger.error("数据库异常:" + e.getMessage());
            jsos.put("result", "1");
            jsos.put("message", "数据库异常请稍后再试");
            return jsos;
        }
        // 若用户没有选择发布区域，则直接返回信息保存成功
        if (areaIdList == null || areaIdList.length == 0) {
            jsos.put("result", "0");
            jsos.put("message", "信息修改成功");
            return jsos;
        }
        // 创建广告-区域关联对象实例
        try {
            List<AdvMappingModel> tempList = new ArrayList<AdvMappingModel>();
            for (String areaId : areaIdList) {
                if (StringUtils.isEmpty(areaId)) {
                    continue;
                }
                AdvMappingModel advMappingModel = new AdvMappingModel();
                advMappingModel.setCreator(um.getUserCode());
                advMappingModel.setAreaId(areaId);
                advMappingModel.setImgId(advInfoModel.getId());
                tempList.add(advMappingModel);
                if (tempList.size() >= 200) {
                    advertisementDao.saveAdvMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                advertisementDao.saveAdvMapping(tempList);
            }
        } catch (Exception e) {
            logger.error("数据库异常:" + e.getMessage());
            jsos.put("result", "1");
            jsos.put("message", "数据库异常请稍后再试");
            return jsos;
        }
        jsos.put("result", "0");
        jsos.put("message", "信息修改成功");
        return jsos;
    }

    /**
     * 删除广告信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/deladvinfo")
    @ResponseBody
    public JSONObject deladvinfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsos = new JSONObject();
        // 获取参数
        String advImgIds = request.getParameter("advImgIds");
        String urls = request.getParameter("urls");
        String[] idArry = advImgIds.split(",");
        String[] urlArry = urls.split(",");
        List<String> idlist = Arrays.asList(idArry);
        List<String> urlList = Arrays.asList(urlArry);
        // 连数据库删除广告信息
        try {
            advertisementDao.delAdvInfo(idlist);
            for (String imgId : idlist) {
                AdvInfoModel advInfoModel = new AdvInfoModel();
                advInfoModel.setId(imgId);
                advertisementDao.delAdvMapping(advInfoModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsos.put("result", "1");
            jsos.put("message", "数据库异常请稍后再试");
            return jsos;
        }
        // 删除图片信息
        try {
            for (String url : urlList) {
                deleteImg(url.split("fileID=")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsos.put("result", "1");
            jsos.put("message", "图片服务器删除广告图片失败");
            return jsos;
        }
        jsos.put("result", "0");
        jsos.put("message", "信息删除成功");
        return jsos;
    }

    /**
     * 根据权限生成toolbar
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getToolbar")
    @ResponseBody
    public JSONObject getToolbar(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        if (null != listbutton && listbutton.size() > 0) {
            jsonObj.put("result", listbutton);
        }
        return jsonObj;
    }
}
