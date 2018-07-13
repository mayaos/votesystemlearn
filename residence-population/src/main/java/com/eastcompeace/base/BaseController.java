package com.eastcompeace.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.dto.HeadDto;
import com.eastcompeace.dto.ReturnDto;
import com.eastcompeace.enums.RespCodeEnum;

/**
 * Controller基类 
 */
@Controller
public class BaseController {
	private Logger logger = Logger.getLogger("BaseController");
	
    /**
     * 
     * @MethodName sloveExp
     * @Date   2018年5月24日下午2:44:54
     * @author wzh
     *
     * 统一异常处理机制
     * @param e
     * @param response
     * @return String
     */
//    @ExceptionHandler(Exception.class)
//    public ReturnDto<Object> sloveExp(Exception e, HttpServletResponse response) {
//        logger.error(this, e);
//        return returnFailJson(e.getMessage());
//    }
	
	/**
	 * 读取字节流中的数据返回字符串
	 * @param request HttpServletRequest对象
	 * @return
	 * @throws IOException 
	 */
	public String getText(HttpServletRequest request) throws IOException {
		return getText(request, "UTF-8");
	}
	
	/**
	 * 读取字节流中的数据返回字符串
	 * @param request HttpServletRequest对象
	 * @param encoded 编码
	 * @return
	 * @throws IOException 
	 */
	public String getText(HttpServletRequest request, String encoded) throws IOException {
		return IOUtils.toString(request.getInputStream(), encoded);
	}
	
    /**
     * 组拼正确返回报文,有body
     */
    public ReturnDto<Object> returnSuccessJson(Object obj) {
        ReturnDto<Object> dto = new ReturnDto<Object>();
        HeadDto head = new HeadDto();
        head.setRspCode(RespCodeEnum.COMMON_SUSSESS.getCode());
        head.setRspMessage(RespCodeEnum.COMMON_SUSSESS.getMessage());
        dto.setHeader(head);
        dto.setBody(obj);
        return dto;
    }
    
    /**
     * 组拼正确返回报文,无body
     * 
     * @return
     */
    public ReturnDto<Object> returnSuccessJson() {
        ReturnDto<Object> dto = new ReturnDto<Object>();
        HeadDto head = new HeadDto();
        head.setRspCode(RespCodeEnum.COMMON_SUSSESS.getCode());
        head.setRspMessage(RespCodeEnum.COMMON_SUSSESS.getMessage());
        dto.setHeader(head);
        return dto;
    }
    
    /**
     * 返回错误信息到客户端
     * 
     * @param se 自定义ServiceException异常
     */
    public ReturnDto<Object> returnFailJson(String exMsg) {
        ReturnDto<Object> dto = new ReturnDto<Object>();
        HeadDto head = new HeadDto();
        head.setRspCode(RespCodeEnum.COMMON_ERROR.getCode());
        head.setRspMessage(exMsg);
        dto.setHeader(head);
        return dto;
    }
    
    /**
     * 
     * @MethodName returnFailJson
     * @Date   2018年6月14日上午9:33:08
     * @author wzh
     *
     * 返回错误信息到客户端
     * @param exCode		//错误代码
     * @param exMsg			//错误信息
     * @return ReturnDto<Object>
     */
    public ReturnDto<Object> returnFailJson(String exCode,String exMsg) {
        ReturnDto<Object> dto = new ReturnDto<Object>();
        HeadDto head = new HeadDto();
        head.setRspCode(exCode);
        head.setRspMessage(exMsg);
        dto.setHeader(head);
        return dto;
    }
    
	/**
	 * 根据传入的模块路径，获取相对根路径url
	 * @param url 传入路径
	 * @return
	 */
	public ModelAndView getModelView(String url) {
		if (url.indexOf(".") > 0) {
			
		} else {
			if (!url.endsWith("/")) url += "/";
		}
		
		return new ModelAndView("../" + url);
	}
}