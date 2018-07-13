package com.eastcompeace.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.eastcompeace.base.ServiceException;
import com.eastcompeace.dto.SuggestInfoDto;
import com.eastcompeace.dto.SuggestTypeDto;
import com.eastcompeace.util.NetUtils;
import com.eastcompeace.util.ResourceUtils;

@Service
public class SuggestService {
	private static Log logger = LogFactory.getLog(SuggestService.class);

	public SuggestTypeDto getSuggestList(String targetPage)throws Exception{
		//1.校验目标页数是否正确
		String url = ResourceUtils.getProperty("source_web_url")+"/guestbook.jspx?app=100";
		Document doc = Jsoup.connect(url).get();
		int totalPage = Integer.valueOf(doc.select("totalPage").text());
		int targetPageInt = Integer.valueOf(targetPage);
		if(targetPageInt<0 || targetPageInt>totalPage){
			throw new ServiceException("目标页数超出范围!");
		}
		//2.拼装URL并连接
		//http://zjldrk.zjsgat.gov.cn/guestbook.jspx?app=100
		String jspxVal = "guestbook.jspx";
		if(targetPageInt>1){
			jspxVal = "guestbook_"+targetPageInt+".jspx";
		}
		url = ResourceUtils.getProperty("source_web_url")+"/"+jspxVal+"?app=100";
		logger.info(">>>意见建议信息集合URL："+url);
		doc = Jsoup.connect(url).get();
		//3.拼装意见建议信息集合内容并返回
		SuggestTypeDto suggestTypeDto = new SuggestTypeDto();
		suggestTypeDto.setTotalCount(doc.select("totalCount").text());
		suggestTypeDto.setCurrentPage(doc.select("currentPage").text());
		suggestTypeDto.setTotalPage(doc.select("totalPage").text());
		List<SuggestInfoDto> suggestInfoList = new ArrayList<SuggestInfoDto>();
		SuggestInfoDto suggestInfoDto = null;
		Elements els = doc.select("dataList");
		for (Iterator<Element> it = els.iterator(); it.hasNext();) {
			Element e = (Element)it.next();
			suggestInfoDto = new SuggestInfoDto();
			suggestInfoDto.setSugType(e.select("sugType").text());
			suggestInfoDto.setSugUser(e.select("sugUser").text());
			suggestInfoDto.setSugTime(e.select("sugTime").text());
			suggestInfoDto.setSugCont(e.select("sugCont").text());
			suggestInfoDto.setSugRepl(e.select("sugRepl").text());
			suggestInfoList.add(suggestInfoDto);
		}
		suggestTypeDto.setDataList(suggestInfoList);
		return suggestTypeDto;
	}
	
	/**
	 * 
	 * @MethodName submitSuggest
	 * @Date   2018年6月13日下午2:21:55
	 * @author wzh
	 *
	 * 提交意见建议信息
	 * @param requestjso	//请求字符串(JSON格式)
	 * @return String		//提交结果码
	 */
	public JSONObject submitSuggest(JSONObject requestjso)throws Exception{
		//拼装参数
		StringBuilder requestParams = new StringBuilder();
		//添加调用总队官网接口唯一标识
		requestParams.append("fromCompany=gxdata&");
		requestParams.append("ctgId="+requestjso.get("ctgId")+"&");
		requestParams.append("email="+requestjso.get("email")+"&");
		requestParams.append("phone="+requestjso.get("phone")+"&");
		requestParams.append("qq="+requestjso.get("qq")+"&");
		requestParams.append("title="+requestjso.get("title")+"&");
		requestParams.append("content="+requestjso.get("content"));
		logger.info(">>>调用意见建议接口参数："+requestParams.toString());
		String requestURL = ResourceUtils.getProperty("source_web_url")+"/guestbook.jspx";
		logger.info(">>>提交意见建议接口URL："+requestURL);
		String returnStr = NetUtils.sendPost(requestURL,requestParams.toString());
		JSONObject jsonObj = JSONObject.fromObject(returnStr);
		logger.info(">>>请求结果集:"+jsonObj.toString());
		return jsonObj;
	}
	
}
