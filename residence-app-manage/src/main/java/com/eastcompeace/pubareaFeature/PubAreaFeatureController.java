package com.eastcompeace.pubareaFeature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
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
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.AreaFeatureMappingModel;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.PubAreaFeatureModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Controller
@RequestMapping("/pubareaFeaturectrl")
public class PubAreaFeatureController extends BaseController {
    private Logger     logger = Logger.getLogger("PubAreaFeatureController");
    @Resource
    private SqlSession sqlSession;

    /**
     * 地方特色表基础页面跳转
     * 
     * @return
     */
    @RequestMapping("/showindex")
    public ModelAndView codedictIndex() {
        return super.getModelView("common/pubareaFeature/");
    }

    /**
     * 查询地方特色列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/areaFeaturelist")
    @ResponseBody
    public JSONObject queryareaFeature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        PubAreaFeatureDao pt = sqlSession.getMapper(PubAreaFeatureDao.class);
        try {
            String strRows = request.getParameter("rows");
            String strPage = request.getParameter("page");
            PubAreaFeatureModel model = new PubAreaFeatureModel();
            model.setAreaFeatureType(request.getParameter("areaFeatureType"));
            model.setAreaFeatureTitle(request.getParameter("areaFeatureTitle"));
            model.setIssueTime(request.getParameter("issueTime"));
            model.setCreateTime(request.getParameter("createTime"));
            model.setAreaFeatureBar(request.getParameter("areaFeatureBar"));
            PageHelper.startPage(Integer.parseInt(strPage), Integer.parseInt(strRows));
            List<PubAreaFeatureModel> areaFeaturelist = pt.selAreaFeature(model);
            for (PubAreaFeatureModel p : areaFeaturelist) {
                // 判断是否置顶
               if (p.getAreaFeatureBar().equals("1")) {
                   p.setAreaFeatureBar("是");
               }
               else if(p.getAreaFeatureBar().equals("0")){
                   p.setAreaFeatureBar("否");
               }
                // 赋值地方特色图片
                if (p.getAreaFeaturePic() != null && !"".equals(p.getAreaFeaturePic())) {
                    p.setAreaFeaturePic(ResourceUtils.getProperty("httpDownloadURL") + p.getAreaFeaturePic());
                }
                // 赋值区域信息
                List<AreaModel> areaList = pt.listAreaName(p.getAreaFeatureId());
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
                    p.setAreaNameList(areaNameBuff.substring(0, areaNameBuff.length() - 1).toString());
                    // 清空areaNameBuff内容
                    areaNameBuff.delete(0, areaNameBuff.length());
                }
                if (areaIdBuff.length() > 0) {
                    // 除了末尾逗号，其余字符都转成字符串
                    p.setAreaIdList(areaIdBuff.substring(0, areaIdBuff.length() - 1).toString());
                    // 清空areaIdBuff内容
                    areaIdBuff.delete(0, areaIdBuff.length());
                }
            }
            PageInfo<PubAreaFeatureModel> pages = new PageInfo(areaFeaturelist);
            jsn.put("total", pages.getTotal());
            jsn.put("rows", areaFeaturelist);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询咨询提示分页:" + e);
        }
        return jsn;
    }

    /**
     * 添加地方特色信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addareaFeature")
    @ResponseBody
    public JSONObject addareaFeature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        UserModel user = new UserModel();
        PubAreaFeatureDao ptd = sqlSession.getMapper(PubAreaFeatureDao.class);
        HttpSession session = request.getSession();
        user = (UserModel) session.getAttribute(Constant.SESSION_USER);
        PubAreaFeatureModel ptm = new PubAreaFeatureModel();
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        MultipartFile upImgsrc = multipartRequest.getFile("areaFeatureImg");
        String areaFeatureTitle = multipartRequest.getParameter("areaFeatureTitle");
        String areaFeatureFrom = multipartRequest.getParameter("areaFeatureFrom");
        String issueTime = multipartRequest.getParameter("issueTime");
        String areaFeatureDesc = multipartRequest.getParameter("areaFeatureDesc");
        String areaFeatureContent = multipartRequest.getParameter("areaFeatureContent");
        String areaFeatureType = multipartRequest.getParameter("areaFeatureType");
        String rank = multipartRequest.getParameter("rank");
        String[] areaIdList = multipartRequest.getParameterValues("areaId");
        String fileId = "";
        if (upImgsrc.getOriginalFilename() != "") { // 有附件则保存图片
            fileId = uploadImage(upImgsrc, "", "");
        }
        ptm.setCreateTime(DateUtils.getNow());
        ptm.setIssueTime(issueTime);
        ptm.setAreaFeatureFrom(areaFeatureFrom);
        ptm.setAreaFeatureContent(areaFeatureContent);
        ptm.setAreaFeatureTitle(areaFeatureTitle);
        ptm.setUserId(user.getUserId());
        ptm.setAreaFeaturePic(fileId);
        ptm.setAreaFeatureDesc(areaFeatureDesc);
        ptm.setAreaFeatureType(areaFeatureType);
        ptm.setRank(rank);
        try {
            ptd.addAreaFeature(ptm);
        } catch (Exception e) {
            jsn.put("result", "1");
            jsn.put("message", "添加失败");
            return jsn;
        }
        // 获取上传管理员信息
        UserModel um = UserUtil.getUser(request);
        // 创建地方特色-区域关联对象实例
        List<AreaFeatureMappingModel> tempList = new ArrayList<AreaFeatureMappingModel>();
        try {
            for (String areaId : areaIdList) {
                AreaFeatureMappingModel areaFeatureMappingModel = new AreaFeatureMappingModel();
                areaFeatureMappingModel.setCreator(um.getUserCode());
                areaFeatureMappingModel.setAreaId(areaId);
                areaFeatureMappingModel.setAreaFeatureId(ptm.getAreaFeatureId());
                tempList.add(areaFeatureMappingModel);
                if (tempList.size() >= 200) {
                    ptd.saveAreaFeatureMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                ptd.saveAreaFeatureMapping(tempList);
            }
        } catch (Exception e) {
            logger.error("数据库异常:" + e.getMessage());
            jsn.put("result", "1");
            jsn.put("message", "添加失败");
            return jsn;
        }
        jsn.put("result", "0");
        jsn.put("message", "添加成功");
        return jsn;
    }

    /**
     * 删除地方特色信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delareaFeature")
    @ResponseBody
    public JSONObject delareaFeature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObj = new JSONObject();
        PubAreaFeatureDao ptd = sqlSession.getMapper(PubAreaFeatureDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("areaFeatureId");
        String[] idarry = log_ids.split(",");
        List<String> list = Arrays.asList(idarry);
        try {
            // 定义地方特色集合，用于获取需要删除的地方特色标题图片
            List<PubAreaFeatureModel> areaFeatureList = ptd.queryAreaFeaturePic(list);
            // 循环删除服务器上的地方特色图片
            for (PubAreaFeatureModel p : areaFeatureList) {
                if (p.getAreaFeaturePic() != null && !"".equals(p.getAreaFeaturePic())) {
                    deleteImg(p.getAreaFeaturePic());
                }
            }
            // 批量删除关联区域
            for (String areaFeatureId : list) {
                ptd.delAreaFeatureAreaMapping(Integer.parseInt(areaFeatureId));
            }
            // 批量删除数据库的地方特色信息
            ptd.delAreaFeature(list);
            jsonObj.put("result", "0");
            jsonObj.put("message", "删除记录成功");
            logDao.add(BuildModel.getModel("Y", "删除咨询提示", String.valueOf(Logenum.DELETEINFO), request));
            return jsonObj;
        } catch (Exception e) {
            jsonObj.put("result", "1");
            jsonObj.put("message", "删除记录失败" + e.getMessage());
            logDao.add(BuildModel.getModel("N", "删除咨询提示", String.valueOf(Logenum.DELETEINFO), request));
            logger.info("删除咨询提示:" + e);
            return jsonObj;
        }
    }

    /**
     * 修改地方特色信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/editareaFeature")
    @ResponseBody
    public JSONObject editareaFeature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        PubAreaFeatureDao ptd = sqlSession.getMapper(PubAreaFeatureDao.class);
        PubAreaFeatureModel model = new PubAreaFeatureModel();
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        // 获取参数
        MultipartFile upImgsrc = multipartRequest.getFile("areaFeatureImg2");
        String areaFeatureId = multipartRequest.getParameter("areaFeatureId");
        String areaFeatureTitle = multipartRequest.getParameter("areaFeatureTitle2");
        String areaFeatureFrom = multipartRequest.getParameter("areaFeatureFrom2");
        String areaFeatureContent = multipartRequest.getParameter("areaFeatureContent");
        String areaFeatureDesc = multipartRequest.getParameter("areaFeatureDesc2");
        String areaFeatureType = multipartRequest.getParameter("areaFeatureType2");
        String rank = multipartRequest.getParameter("rank2");
        String[] areaIdList = multipartRequest.getParameterValues("areaId2");
        // 检查所属区域是否为空
        if (areaIdList == null || areaIdList.length == 0) {
            jsn.put("result", "1");
            jsn.put("message", "所属区域不能为空！");
            return jsn;
        }
        // 检查参数
        if (areaFeatureId == null || "".equals(areaFeatureId) || !areaFeatureId.matches("\\d+")) {
            jsn.put("result", "1");
            jsn.put("message", "无法获取地方特色Id,请重新选择需要修改的数据！");
            return jsn;
        }
        // 给model赋值
        model.setAreaFeatureId(Integer.parseInt(areaFeatureId));
        model.setAreaFeatureTitle(areaFeatureTitle);
        model.setAreaFeatureFrom(areaFeatureFrom);
        model.setAreaFeatureContent(areaFeatureContent);
        model.setUpdateTime(DateUtils.getNow());
        model.setAreaFeatureDesc(areaFeatureDesc);
        model.setAreaFeatureType(areaFeatureType);
        model.setRank(rank);
        try {
            PubAreaFeatureModel selectModel = new PubAreaFeatureModel(Integer.parseInt(areaFeatureId));
            List<PubAreaFeatureModel> list = ptd.selAreaFeature(selectModel);
            if (list == null || list.size() != 1) {
                jsn.put("result", "1");
                jsn.put("message", "数据库异常无法修改数据,请稍后再试！");
                return jsn;
            }
            // 由于list只有一个元素所以直接获取原标题图片Id
            if (upImgsrc != null) {
                for (PubAreaFeatureModel p : list) {
                    // 保存新标题图片到服务器并删除旧图片，然后把新图片的Id保存到model
                    model.setAreaFeaturePic(uploadImage(upImgsrc, p.getAreaFeaturePic(), ""));
                }
            }
            // 删除原有所属区域
            ptd.delAreaFeatureAreaMapping(Integer.parseInt(areaFeatureId));
            // 新增所属区域
            // 获取上传管理员信息
            UserModel um = UserUtil.getUser(request);
            // 创建地方特色-区域关联对象实例
            List<AreaFeatureMappingModel> tempList = new ArrayList<AreaFeatureMappingModel>();
            for (String areaId : areaIdList) {
                AreaFeatureMappingModel areaFeatureMappingModel = new AreaFeatureMappingModel();
                areaFeatureMappingModel.setCreator(um.getUserCode());
                areaFeatureMappingModel.setAreaId(areaId);
                areaFeatureMappingModel.setAreaFeatureId(Integer.parseInt(areaFeatureId));
                tempList.add(areaFeatureMappingModel);
                if (tempList.size() >= 200) {
                    ptd.saveAreaFeatureMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                ptd.saveAreaFeatureMapping(tempList);
            }
            // 保存地方特色修改的信息
            ptd.editAreaFeature(model);
            jsn.put("result", "0");
            jsn.put("message", "修改成功");
            return jsn;
        } catch (Exception e) {
            jsn.put("result", "1");
            jsn.put("message", "修改失败" + e.getMessage());
            logger.info("修改咨询提示:" + e);
            return jsn;
        }
    }

    /**
     * 查询指南文章信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/querycontent")
    @ResponseBody
    public JSONObject querycontent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObj = new JSONObject();
        PubAreaFeatureDao ptd = sqlSession.getMapper(PubAreaFeatureDao.class);
        PubAreaFeatureModel ptm = new PubAreaFeatureModel();
        ;
        String id = request.getParameter("iId");
        try {
            ptm = ptd.querycontent(id);
        } catch (Exception e) {
            jsonObj.put("result", "1");
            jsonObj.put("message", "查询失败" + e.getMessage());
            return jsonObj;
        }
        JSONObject jsonObject = JSONObject.fromObject(ptm);
        jsonObj.put("areaFeature", jsonObject);
        jsonObj.put("result", "0");
        jsonObj.put("message", "查询成功");
        return jsonObj;
    }
    /**
     * 设置为置顶
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @RequestMapping("/setBar")
    @ResponseBody    
    public JSONObject setBar(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject jsonObj = new JSONObject();
        PubAreaFeatureDao ptd = sqlSession.getMapper(PubAreaFeatureDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("areaFeatureId");
        String[] idarry = log_ids.split(",");
        List<String> list = Arrays.asList(idarry);
        ptd.updateBar(list);
        jsonObj.put("result", "0");
        jsonObj.put("message", "添加置顶成功");
        return jsonObj;
    }
    
    /**
     * 取消为置顶
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @RequestMapping("/removeBar")
    @ResponseBody    
    public JSONObject removeBar(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject jsonObj = new JSONObject();
        PubAreaFeatureDao ptd = sqlSession.getMapper(PubAreaFeatureDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("areaFeatureId");
        String[] idarry = log_ids.split(",");
        List<String> list = Arrays.asList(idarry);
        ptd.removeBar(list);
        jsonObj.put("result", "0");
        jsonObj.put("message", "取消置顶成功");
        return jsonObj;
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
        return super.getToolbar(request, sqlSession);
    }
}
