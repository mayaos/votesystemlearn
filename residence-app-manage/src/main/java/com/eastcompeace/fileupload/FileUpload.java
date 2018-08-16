package com.eastcompeace.fileupload;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.eastcompeace.base.BaseController;
import com.eastcompeace.util.ResourceUtils;
/**
 * CKEdit图片上传
 * @author xianzehua
 *
 */
@Controller
@RequestMapping("/fileUploadCtrl")
public class FileUpload extends BaseController {
	private Logger logger = Logger.getLogger("OrgArticleController");
	
	 /**
     * ckeditor图片上传
     * 
     * @param request
     * @param response
     */
    @RequestMapping("imageUpload")
    public void imageUpload(HttpServletRequest request, HttpServletResponse response) {
        try {
        	upload(request, response);
        } catch (Exception e) {
        	logger.error(this, e);
            e.printStackTrace();
        } 
    }
    
    /**
     * 图片上传
     * 
     * @Title upload
     * @param request
     * @param response
     * @throws Exception
     */
    private void upload(HttpServletRequest request, HttpServletResponse response) throws Exception,
            IOException {
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
                .getServletContext());
        // 图片名称
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            
         // 建立多部分请求解析器
    		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
    		MultipartHttpServletRequest multiRequest = resolver.resolveMultipart(request);
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                
                if (file != null) {
                	//获取文件存储路径，存储在resources/configure.properties
            		//图片保存目录路径	
            		String imgUrl = ResourceUtils.getProperty("httpDownloadURL");
                	String fileName = uploadImage(file, "", "");
                	
                	String imageContextPath = imgUrl + fileName;
                    response.setContentType("text/html;charset=UTF-8");
                    String callback = request.getParameter("CKEditorFuncNum");
                    PrintWriter out = response.getWriter();
                    out.println("<script type=\"text/javascript\">");
                    out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + imageContextPath + "',''" + ")");
                    out.println("</script>");
                    out.flush();
                    out.close();
                }
            }
        }
    }
}
