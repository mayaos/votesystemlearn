package com.eastcompeace.pubFAQ;

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
import org.springframework.web.servlet.ModelAndView;
import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.FAQModel;
import com.eastcompeace.model.FAQModel;
import com.eastcompeace.model.OrgArticleModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.org.article.OrgArticleDao;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 常见问题信息表（增、删、改、查）
 * @author caiyaonan
 *
 */
@Controller
@RequestMapping("/FAQctrl")
public class PubFAQController extends BaseController{
	private Logger logger = Logger.getLogger("PubFAQController");
	@Resource
	private SqlSession sqlSession;
	/**
	 * 常见问题信息表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/pubFAQ/");
	}
	
	/**
	 * 查询常见问题表列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/FAQlist")
	@ResponseBody
	public JSONObject queryGuide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn = new JSONObject();
		PubFAQDao pgd = sqlSession.getMapper(PubFAQDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		FAQModel pgm = new FAQModel();
		pgm.setCreateTime(request.getParameter("createTime"));
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<FAQModel> guidelist = pgd.selectFAQ(pgm);
			PageInfo<FAQModel> pages = new PageInfo(guidelist);
			jsn.put("total", pages.getTotal());
			jsn.put("rows",guidelist);
			logDao.add(BuildModel.getModel( "Y", "查询常见问题分页", String.valueOf(Logenum.QUERYLIST), request));
		} catch (Exception e) {
			logDao.add(BuildModel.getModel( "N", "查询常见问题分页", String.valueOf(Logenum.QUERYLIST), request));
			logger.info("查询常见问题分页:"+e);
		}
		return jsn;
	}
	/**
	 * 添加常见问题信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addFAQ")
	@ResponseBody
	public JSONObject addguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		UserModel user = new UserModel();
		HttpSession session = request.getSession();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		JSONObject jsn = new JSONObject();
		PubFAQDao pgd = sqlSession.getMapper(PubFAQDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		FAQModel pgm = new FAQModel();
		pgm.setCreateTime(DateUtils.getNow());
		pgm.setUpdateTime(DateUtils.getNow());
		pgm.setAnswers(request.getParameter("answers"));
		pgm.setIssueFlag(0);
		pgm.setOrderNo(request.getParameter("orderNo"));
		pgm.setQuestions(request.getParameter("questions"));
		try{
			pgd.addFAQ(pgm);
			logDao.add(BuildModel.getModel( "Y", "添加常见问题", String.valueOf(Logenum.ADDINFO), request));
			jsn.put("result","0");
			jsn.put("message","添加成功");
			return jsn;
		}catch(Exception e){
			jsn.put("result","1");
			jsn.put("message","添加失败");
			logDao.add(BuildModel.getModel( "N", "添加常见问题", String.valueOf(Logenum.ADDINFO), request));
			logger.info("添加常见问题:"+e);
			return jsn;
		}
		
	}
	/**
	 * 修改常见问题信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editFAQ")
	@ResponseBody
	public JSONObject editguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		UserModel user = new UserModel();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		JSONObject jsn=new JSONObject();
		PubFAQDao pgd = sqlSession.getMapper(PubFAQDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		FAQModel pgm = new FAQModel();
		pgm.setId(Integer.parseInt(request.getParameter("id")));
        pgm.setAnswers(request.getParameter("answers"));
        pgm.setIssueFlag(0);
        pgm.setOrderNo(request.getParameter("orderNo"));
        pgm.setQuestions(request.getParameter("questions"));
		pgm.setUpdateTime(DateUtils.getNow());
		try{
			pgd.updateFAQ(pgm);
			jsn.put("result","0");
			jsn.put("message","修改成功");
			logDao.add(BuildModel.getModel( "Y", "修改常见问题", String.valueOf(Logenum.EDITINFO), request));
			return jsn;
		}catch(Exception e){
			jsn.put("result","1");
			jsn.put("message","修改失败");
			logDao.add(BuildModel.getModel( "N", "修改常见问题", String.valueOf(Logenum.EDITINFO), request));
			logger.info("修改常见问题:"+e);
			return jsn;
		}
	
	}
	/**
	 * 删除常见问题信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delFAQ")
	@ResponseBody
	public JSONObject delguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		PubFAQDao pgd = sqlSession.getMapper(PubFAQDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String log_ids = request.getParameter("id");
		String[] idarry = log_ids.split(",");
		List list = Arrays.asList(idarry);
		try{
			pgd.delFAQ(list);
			jsonObj.put("result","0");
			jsonObj.put("message","删除记录成功");
			logDao.add(BuildModel.getModel( "Y", "删除常见问题", String.valueOf(Logenum.DELETEINFO), request));
			return jsonObj;
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","删除记录失败");
			logDao.add(BuildModel.getModel( "N", "删除常见问题", String.valueOf(Logenum.DELETEINFO), request));
			logger.info("删除常见问题:"+e);
			return jsonObj;
		}
		
	} 
	
	
    
    /**
     * 发布问题
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/releaseFAQ")
    @ResponseBody
    public JSONObject releaseArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
    
        JSONObject jsos = new JSONObject();
        PubFAQDao pubFAQDao = sqlSession.getMapper(PubFAQDao.class);
        //获取需要发布的记录articlId
        String ids = request.getParameter("id");

        //把articlId字符串拆分为数组，数据转换为List
        String[] idArr = ids.split(",");
        List list = Arrays.asList(idArr);
        
        //定义文章信息实例
        List<OrgArticleModel> rightsList = new ArrayList<OrgArticleModel>();
            
        try{
            //批量发布数据
            pubFAQDao.releaseFAQ(list);
        }catch(Exception e){
            logger.error("发布问题失败:" + e.getMessage());
            jsos.put("result","1");
            jsos.put("message","发布问题失败，请稍后再试！");
            return jsos;
        }
        
        jsos.put("result","0");
        jsos.put("message","发布问题成功");
        return jsos;
        
    }
    /**
     * 取消发布问题
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delreleaseFAQ")
    @ResponseBody
    public JSONObject delreleaseArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
    
        JSONObject jsos = new JSONObject();
        PubFAQDao pubFAQDao = sqlSession.getMapper(PubFAQDao.class);
        //获取需要发布的记录articlId
        String ids = request.getParameter("id");

        //把articlId字符串拆分为数组，数据转换为List
        String[] idArr = ids.split(",");
        List list = Arrays.asList(idArr);
        
        //定义文章信息实例
        List<OrgArticleModel> rightsList = new ArrayList<OrgArticleModel>();
            
        try{
            //批量发布数据
            pubFAQDao.delreleaseFAQ(list);
        }catch(Exception e){
            logger.error("取消发布问题失败:" + e.getMessage());
            jsos.put("result","1");
            jsos.put("message","取消发布问题失败，请稍后再试！");
            return jsos;
        }
        
        jsos.put("result","0");
        jsos.put("message","取消发布问题成功");
        return jsos;
        
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
	public JSONObject getToolbar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.getToolbar(request, sqlSession);
	}
}
	


