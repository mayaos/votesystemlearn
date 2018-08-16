package com.eastcompeace.pubmerchantIndex;
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
import com.eastcompeace.model.PubMerchantIndexModel;
import com.eastcompeace.model.MerchantIndexMappingModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.system.config.ConfigController;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Controller
@RequestMapping("/pubmerIndexctrl")
public class PubMerchantIndexController extends BaseController {
    private Logger     logger = Logger.getLogger("PubMerchantIndexController");
    @Resource
    private SqlSession sqlSession;

    /**
     * 咨询热点表基础页面跳转
     * 
     * @return
     */
    @RequestMapping("/showindex")
    public ModelAndView codedictIndex() {
        return super.getModelView("common/pubMerchantIndex/");
    }

    /**
     * 查询资讯列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/merchantIndexlist")
    @ResponseBody
    public JSONObject queryMerchantIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        PubMerchantIndexDao pt = sqlSession.getMapper(PubMerchantIndexDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        try {
            String strRows = request.getParameter("rows");
            String strPage = request.getParameter("page");
            PubMerchantIndexModel model = new PubMerchantIndexModel();
            model.setMerchantIndexType(request.getParameter("merchantIndexType"));
            model.setMerchantIndexTitle(request.getParameter("merchantIndexTitle"));
            model.setIssueTime(request.getParameter("issueTime"));
            model.setCreateTime(request.getParameter("createTime"));
            PageHelper.startPage(Integer.parseInt(strPage), Integer.parseInt(strRows));
            List<PubMerchantIndexModel> merchantIndexlist = pt.selmerchantIndex(model);
            for (PubMerchantIndexModel p : merchantIndexlist) {
                // 赋值资讯图片
                if (p.getMerchantIndexPic() != null && !"".equals(p.getMerchantIndexPic())) {
                    p.setMerchantIndexPic(ResourceUtils.getProperty("httpDownloadURL") + p.getMerchantIndexPic());
                }
                // 赋值区域信息
                List<AreaModel> areaList = pt.listAreaName(p.getMerchantIndexId());
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
            PageInfo<PubMerchantIndexModel> pages = new PageInfo(merchantIndexlist);
            jsn.put("total", pages.getTotal());
            jsn.put("rows", merchantIndexlist);
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
    @RequestMapping("/addmerchantIndex")
    @ResponseBody
    public JSONObject addMerchantIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        UserModel user = new UserModel();
        PubMerchantIndexDao ptd = sqlSession.getMapper(PubMerchantIndexDao.class);
        HttpSession session = request.getSession();
        user = (UserModel) session.getAttribute(Constant.SESSION_USER);
        PubMerchantIndexModel ptm = new PubMerchantIndexModel();
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        MultipartFile upImgsrc = multipartRequest.getFile("merchantIndexImg");
        String merchantIndexTitle = multipartRequest.getParameter("merchantIndexTitle");
        String issueTime = multipartRequest.getParameter("issueTime");
        String merchantIndexDesc = multipartRequest.getParameter("merchantIndexDesc");
        String link = multipartRequest.getParameter("link");
        String merchantIndexType = multipartRequest.getParameter("merchantIndexType");
        String rank = multipartRequest.getParameter("rank");
        String[] areaIdList = multipartRequest.getParameterValues("areaId");
        String fileId = "";
        if(upImgsrc!=null){
            if (upImgsrc.getOriginalFilename() != "") { // 有附件则保存图片
                fileId = uploadImage(upImgsrc, "", "");
                ptm.setMerchantIndexPic(fileId);
            }            
        }
        ptm.setLink(link);
        ptm.setCreateTime(DateUtils.getNow());
        ptm.setIssueTime(issueTime);
        ptm.setMerchantIndexTitle(merchantIndexTitle);
        ptm.setUserId(user.getUserId());
        ptm.setMerchantIndexDesc(merchantIndexDesc);
        ptm.setMerchantIndexType(merchantIndexType);
        ptm.setRank(rank);
        try {
            ptd.addmerchantIndex(ptm);
        } catch (Exception e) {
            jsn.put("result", "1");
            jsn.put("message", "添加失败");
            return jsn;
        }
        // 获取上传管理员信息
        UserModel um = UserUtil.getUser(request);
        // 创建资讯-区域关联对象实例
        List<MerchantIndexMappingModel> tempList = new ArrayList<MerchantIndexMappingModel>();
        try {
            for (String areaId : areaIdList) {
                MerchantIndexMappingModel merchantIndexMappingModel = new MerchantIndexMappingModel();
                merchantIndexMappingModel.setCreator(um.getUserCode());
                merchantIndexMappingModel.setAreaId(areaId);
                merchantIndexMappingModel.setMerchantIndexId(ptm.getMerchantIndexId());
                tempList.add(merchantIndexMappingModel);
                if (tempList.size() >= 200) {
                    ptd.saveMerchantIndexMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                ptd.saveMerchantIndexMapping(tempList);
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
    @RequestMapping("/delmerchantIndex")
    @ResponseBody
    public JSONObject delmerchantIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObj = new JSONObject();
        PubMerchantIndexDao ptd = sqlSession.getMapper(PubMerchantIndexDao.class);
        LogDao logDao = sqlSession.getMapper(LogDao.class);
        String log_ids = request.getParameter("merchantIndexId");
        String[] idarry = log_ids.split(",");
        List<String> list = Arrays.asList(idarry);
        try {
            // 定义资讯集合，用于获取需要删除的资讯标题图片
            List<PubMerchantIndexModel> merchantIndexList = ptd.queryMerchantIndexPic(list);
            // 循环删除服务器上的资讯图片
            for (PubMerchantIndexModel p : merchantIndexList) {
                if (p.getMerchantIndexPic() != null && !"".equals(p.getMerchantIndexPic())) {
                    deleteImg(p.getMerchantIndexPic());
                }
            }
            // 批量删除关联区域
            for (String merchantIndexId : list) {
                ptd.delMerchantIndexAreaMapping(Integer.parseInt(merchantIndexId));
            }
            // 批量删除数据库的资讯信息
            ptd.delmerchantIndex(list);
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
    @RequestMapping("/editmerchantIndex")
    @ResponseBody
    public JSONObject editmerchantIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsn = new JSONObject();
        PubMerchantIndexDao ptd = sqlSession.getMapper(PubMerchantIndexDao.class);
        PubMerchantIndexModel model = new PubMerchantIndexModel();
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        // 获取参数
        MultipartFile upImgsrc = multipartRequest.getFile("merchantIndexImg2");
        String merchantIndexId = multipartRequest.getParameter("merchantIndexId");
        String merchantIndexTitle = multipartRequest.getParameter("merchantIndexTitle2");
        String link = multipartRequest.getParameter("link");
        String merchantIndexDesc = multipartRequest.getParameter("merchantIndexDesc2");
        String merchantIndexType = multipartRequest.getParameter("merchantIndexType2");
        String rank = multipartRequest.getParameter("rank2");
        String[] areaIdList = multipartRequest.getParameterValues("areaId2");
        // 检查所属区域是否为空
        if (areaIdList == null || areaIdList.length == 0) {
            jsn.put("result", "1");
            jsn.put("message", "所属区域不能为空！");
            return jsn;
        }
        // 检查参数
        if (merchantIndexId == null || "".equals(merchantIndexId) || !merchantIndexId.matches("\\d+")) {
            jsn.put("result", "1");
            jsn.put("message", "无法获取资讯Id,请重新选择需要修改的数据！");
            return jsn;
        }
        // 给model赋值
        model.setMerchantIndexId(Integer.parseInt(merchantIndexId));
        model.setMerchantIndexTitle(merchantIndexTitle);
        model.setUpdateTime(DateUtils.getNow());
        model.setMerchantIndexDesc(merchantIndexDesc);
        model.setMerchantIndexType(merchantIndexType);
        model.setRank(rank);
        model.setLink(link);
        try {
            PubMerchantIndexModel selectModel = new PubMerchantIndexModel(Integer.parseInt(merchantIndexId));
            List<PubMerchantIndexModel> list = ptd.selmerchantIndex(selectModel);
            if (list == null || list.size() != 1) {
                jsn.put("result", "1");
                jsn.put("message", "数据库异常无法修改数据,请稍后再试！");
                return jsn;
            }
            // 由于list只有一个元素所以直接获取原标题图片Id
            if (upImgsrc != null) {
                for (PubMerchantIndexModel p : list) {
                    // 保存新标题图片到服务器并删除旧图片，然后把新图片的Id保存到model
                    model.setMerchantIndexPic(uploadImage(upImgsrc, p.getMerchantIndexPic(), ""));
                }
            }
            // 删除原有所属区域
            ptd.delMerchantIndexAreaMapping(Integer.parseInt(merchantIndexId));
            // 新增所属区域
            // 获取上传管理员信息
            UserModel um = UserUtil.getUser(request);
            // 创建资讯-区域关联对象实例
            List<MerchantIndexMappingModel> tempList = new ArrayList<MerchantIndexMappingModel>();
            for (String areaId : areaIdList) {
                MerchantIndexMappingModel merchantIndexMappingModel = new MerchantIndexMappingModel();
                merchantIndexMappingModel.setCreator(um.getUserCode());
                merchantIndexMappingModel.setAreaId(areaId);
                merchantIndexMappingModel.setMerchantIndexId(Integer.parseInt(merchantIndexId));
                tempList.add(merchantIndexMappingModel);
                if (tempList.size() >= 200) {
                    ptd.saveMerchantIndexMapping(tempList);
                    tempList.clear();
                }
            }
            if (tempList.size() > 0) {
                ptd.saveMerchantIndexMapping(tempList);
            }
            // 保存资讯修改的信息
            ptd.editmerchantIndex(model);
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
