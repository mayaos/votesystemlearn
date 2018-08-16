package com.eastcompeace.pubtopic;
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
import com.eastcompeace.model.AdvMappingModel;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.PubTopicModel;
import com.eastcompeace.model.TopicMappingModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.system.config.ConfigController;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Controller
@RequestMapping("/pubtopctrl")
public class PubTopicController extends BaseController {
    private Logger     logger = Logger.getLogger("PubTopicController");
    @Resource
    private SqlSession sqlSession;

    /**
     * 咨询热点表基础页面跳转
     * 
     * @return
     */
    @RequestMapping("/showindex")
    public ModelAndView codedictIndex() {
        return super.getModelView("common/pubtopic/");
    }

    /**
     * 查询资讯列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/topiclist")
    @ResponseBody
    public JSONObject queryTopic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        PubTopicDao pt = sqlSession.getMapper(PubTopicDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        try {
            String strRows = request.getParameter("rows");
            String strPage = request.getParameter("page");
            PubTopicModel model = new PubTopicModel();
            model.setTopicType(request.getParameter("topicType"));
            model.setTopicTitle(request.getParameter("topicTitle"));
            model.setIssueTime(request.getParameter("issueTime"));
            model.setCreateTime(request.getParameter("createTime"));
            model.setTopicBar(request.getParameter("topicBar"));
            PageHelper.startPage(Integer.parseInt(strPage), Integer.parseInt(strRows));
            List<PubTopicModel> topiclist = pt.seltopic(model);
            for (PubTopicModel p : topiclist) {
                // 判断是否置顶
               if (p.getTopicBar().equals("1")) {
                   p.setTopicBar("是");
               }
               else if(p.getTopicBar().equals("0")){
                   p.setTopicBar("否");
               }
                // 赋值资讯图片
                if (p.getTopicPic() != null && !"".equals(p.getTopicPic())) {
                    p.setTopicPic(ResourceUtils.getProperty("httpDownloadURL") + p.getTopicPic());
                }
                // 赋值区域信息
                List<AreaModel> areaList = pt.listAreaName(p.getTopicId());
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
            PageInfo<PubTopicModel> pages = new PageInfo(topiclist);
            jsn.put("total", pages.getTotal());
            jsn.put("rows", topiclist);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询咨询提示分页:" + e);
        }
        return jsn;
    }

    /**
     * 添加资讯信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addtopic")
    @ResponseBody
    public JSONObject addTopic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        UserModel user = new UserModel();
        PubTopicDao ptd = sqlSession.getMapper(PubTopicDao.class);
        HttpSession session = request.getSession();
        user = (UserModel) session.getAttribute(Constant.SESSION_USER);
        PubTopicModel ptm = new PubTopicModel();
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        MultipartFile upImgsrc = multipartRequest.getFile("topicImg");
        String topicTitle = multipartRequest.getParameter("topicTitle");
        String topicFrom = multipartRequest.getParameter("topicFrom");
        String issueTime = multipartRequest.getParameter("issueTime");
        String topicDesc = multipartRequest.getParameter("topicDesc");
        String topicContent = multipartRequest.getParameter("topicContent");
        String topicType = multipartRequest.getParameter("topicType");
        String rank = multipartRequest.getParameter("rank");
        String[] areaIdList = multipartRequest.getParameterValues("areaId");
        String fileId = "";
        if (upImgsrc.getOriginalFilename() != "") { // 有附件则保存图片
            fileId = uploadImage(upImgsrc, "", "");
        }
        ptm.setCreateTime(DateUtils.getNow());
        ptm.setIssueTime(issueTime);
        ptm.setTopicFrom(topicFrom);
        ptm.setTopicContent(topicContent);
        ptm.setTopicTitle(topicTitle);
        ptm.setUserId(user.getUserId());
        ptm.setTopicPic(fileId);
        ptm.setTopicDesc(topicDesc);
        ptm.setTopicType(topicType);
        ptm.setRank(rank);
        try {
            ptd.addtopic(ptm);
        } catch (Exception e) {
            jsn.put("result", "1");
            jsn.put("message", "添加失败");
            return jsn;
        }
        // 获取上传管理员信息
        UserModel um = UserUtil.getUser(request);
        // 创建资讯-区域关联对象实例
        List<TopicMappingModel> tempList = new ArrayList<TopicMappingModel>();
        try {
            for (String areaId : areaIdList) {
                TopicMappingModel topicMappingModel = new TopicMappingModel();
                topicMappingModel.setCreator(um.getUserCode());
                topicMappingModel.setAreaId(areaId);
                topicMappingModel.setTopicId(ptm.getTopicId());
                tempList.add(topicMappingModel);
                if (tempList.size() >= 200) {
                    ptd.saveTopicMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                ptd.saveTopicMapping(tempList);
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
     * 删除资讯信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deltopic")
    @ResponseBody
    public JSONObject deltopic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObj = new JSONObject();
        PubTopicDao ptd = sqlSession.getMapper(PubTopicDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("topicId");
        String[] idarry = log_ids.split(",");
        List<String> list = Arrays.asList(idarry);
        try {
            // 定义资讯集合，用于获取需要删除的资讯标题图片
            List<PubTopicModel> topicList = ptd.queryTopicPic(list);
            // 循环删除服务器上的资讯图片
            for (PubTopicModel p : topicList) {
                if (p.getTopicPic() != null && !"".equals(p.getTopicPic())) {
                    deleteImg(p.getTopicPic());
                }
            }
            // 批量删除关联区域
            for (String topicId : list) {
                ptd.delTopicAreaMapping(Integer.parseInt(topicId));
            }
            // 批量删除数据库的资讯信息
            ptd.deltopic(list);
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
     * 修改资讯信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/edittopic")
    @ResponseBody
    public JSONObject edittopic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        PubTopicDao ptd = sqlSession.getMapper(PubTopicDao.class);
        PubTopicModel model = new PubTopicModel();
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        // 获取参数
        MultipartFile upImgsrc = multipartRequest.getFile("topicImg2");
        String topicId = multipartRequest.getParameter("topicId");
        String topicTitle = multipartRequest.getParameter("topicTitle2");
        String topicFrom = multipartRequest.getParameter("topicFrom2");
        String topicContent = multipartRequest.getParameter("topicContent");
        String topicDesc = multipartRequest.getParameter("topicDesc2");
        String topicType = multipartRequest.getParameter("topicType2");
        String rank = multipartRequest.getParameter("rank2");
        String[] areaIdList = multipartRequest.getParameterValues("areaId2");
        // 检查所属区域是否为空
        if (areaIdList == null || areaIdList.length == 0) {
            jsn.put("result", "1");
            jsn.put("message", "所属区域不能为空！");
            return jsn;
        }
        // 检查参数
        if (topicId == null || "".equals(topicId) || !topicId.matches("\\d+")) {
            jsn.put("result", "1");
            jsn.put("message", "无法获取资讯Id,请重新选择需要修改的数据！");
            return jsn;
        }
        // 给model赋值
        model.setTopicId(Integer.parseInt(topicId));
        model.setTopicTitle(topicTitle);
        model.setTopicFrom(topicFrom);
        model.setTopicContent(topicContent);
        model.setUpdateTime(DateUtils.getNow());
        model.setTopicDesc(topicDesc);
        model.setTopicType(topicType);
        model.setRank(rank);
        try {
            PubTopicModel selectModel = new PubTopicModel(Integer.parseInt(topicId));
            List<PubTopicModel> list = ptd.seltopic(selectModel);
            if (list == null || list.size() != 1) {
                jsn.put("result", "1");
                jsn.put("message", "数据库异常无法修改数据,请稍后再试！");
                return jsn;
            }
            // 由于list只有一个元素所以直接获取原标题图片Id
            if (upImgsrc != null) {
                for (PubTopicModel p : list) {
                    // 保存新标题图片到服务器并删除旧图片，然后把新图片的Id保存到model
                    model.setTopicPic(uploadImage(upImgsrc, p.getTopicPic(), ""));
                }
            }
            // 删除原有所属区域
            ptd.delTopicAreaMapping(Integer.parseInt(topicId));
            // 新增所属区域
            // 获取上传管理员信息
            UserModel um = UserUtil.getUser(request);
            // 创建资讯-区域关联对象实例
            List<TopicMappingModel> tempList = new ArrayList<TopicMappingModel>();
            for (String areaId : areaIdList) {
                TopicMappingModel topicMappingModel = new TopicMappingModel();
                topicMappingModel.setCreator(um.getUserCode());
                topicMappingModel.setAreaId(areaId);
                topicMappingModel.setTopicId(Integer.parseInt(topicId));
                tempList.add(topicMappingModel);
                if (tempList.size() >= 200) {
                    ptd.saveTopicMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                ptd.saveTopicMapping(tempList);
            }
            // 保存资讯修改的信息
            ptd.edittopic(model);
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
        PubTopicDao ptd = sqlSession.getMapper(PubTopicDao.class);
        PubTopicModel ptm = new PubTopicModel();
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
        jsonObj.put("topic", jsonObject);
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
        PubTopicDao ptd = sqlSession.getMapper(PubTopicDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("topicId");
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
        PubTopicDao ptd = sqlSession.getMapper(PubTopicDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("topicId");
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
