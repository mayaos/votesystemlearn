package com.eastcompeace.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.eastcompeace.base.ServiceException;
import com.eastcompeace.dto.ArticleAllTypeDto;
import com.eastcompeace.dto.ArticleInfoDto;
import com.eastcompeace.dto.ArticleTypeDto;
import com.eastcompeace.enums.ArticleTypeEnum;
import com.eastcompeace.util.ResourceUtils;

@Service
public class ArticleService {
	private static Log logger = LogFactory.getLog(ArticleService.class);
	
	/**
	 * 
	 * @MethodName getTargetArticleContent
	 * @Date   2018年5月30日下午2:34:07
	 * @author wzh
	 *
	 * 获取流动人口管理服务网指定文章信息
	 * @param articleType
	 * @param articleID
	 * @return ArticleInfoDto
	 * @throws Exception
	 * 方法操作步骤简述
	 * 1.按照文章类别代码获取对应类别值
	 * 2.拼装URL并连接
	 * 3.拼装文章内容并返回
	 */
	public ArticleInfoDto getTargetArticleContent(String articleType,String articleID) throws Exception {
		//1.按照文章类别代码获取对应类别值
		ArticleTypeEnum articleTypeEnum = ArticleTypeEnum.getByTypeCode(articleType);
		if(articleTypeEnum == null){
			throw new ServiceException("无此文章类别代码!");
		}
		//2.拼装文章URL并连接
		//http://zjldrk.zjsgat.gov.cn/tpxw/4431.htm?app=1
		String url = ResourceUtils.getProperty("source_web_url")+"/"+articleTypeEnum.getTypeValue()+"/"+articleID+".htm?app="+articleTypeEnum.getTypeCode();
		logger.info(">>>文章URL："+url);
		Document doc = Jsoup.connect(url).get();
		
		//3.拼装文章内容并返回
		ArticleInfoDto articleInfoDto = new ArticleInfoDto();
		articleInfoDto.setArticleID(articleID);
		articleInfoDto.setArticleTitle(doc.select("newsTitle").text());
		articleInfoDto.setArticleDate(doc.select("publishTime").text());
		articleInfoDto.setArticleAuthor(doc.select("publisher").text());
		articleInfoDto.setArticleFrom(doc.select("from").text());
		//对文章内容中img的src进行特殊处理,如果src不是以"http://"开头，则加上""http://zjldrk.zjsgat.gov.cn""形成绝对路径。
		Elements imgElements = doc.select("content").select("img");
		for(int i=0;i<imgElements.size();i++){
			Element el = imgElements.get(i);
			String imgSrc =  el.attr("src");
			if(!imgSrc.startsWith("http://")){
				el.removeAttr("src");
				el.attr("src",ResourceUtils.getProperty("source_web_url")+imgSrc);  
			}
		}
		articleInfoDto.setArticleContent(doc.select("content").html().replace("\n","").replace("\"", "'"));
		articleInfoDto.setBeforeArticleTitle(doc.select("prevArticleTitle").text());
		articleInfoDto.setBeforeArticleID(doc.select("prevArticleId").text());
		articleInfoDto.setAfterArticleTitle(doc.select("nextArticle").text());
		articleInfoDto.setAfterArticleID(doc.select("nextArticleId").text());
		return articleInfoDto;
	}
	
	/**
	 * 
	 * @MethodName getTargetTypeArticleList
	 * @Date   2018年5月30日下午5:55:40
	 * @author wzh
	 *
	 * 获取流动人口管理服务网指定类别文章信息集合
	 * @param articleType
	 * @param targetPage
	 * @return ArticleTypeDto
	 * @throws Exception
	 * 方法操作步骤简述
	 * 1.按照文章类别代码获取对应类别值
	 * 2.校验目标页数是否正确
	 * 3.拼装URL并连接
	 * 4.拼装类别文章信息集合内容并返回
	 */
	public ArticleTypeDto getTargetTypeArticleList(String articleType,String targetPage) throws Exception{
		//1.按照文章类别代码获取对应类别值
		ArticleTypeEnum articleTypeEnum = ArticleTypeEnum.getByTypeCode(articleType);
		if(articleTypeEnum == null){
			throw new ServiceException("无此文章类别代码!");
		}
		//2.校验目标页数是否正确
		String url = ResourceUtils.getProperty("source_web_url")+"/"+articleTypeEnum.getTypeValue()+"/index.htm?app="+articleTypeEnum.getTypeCode();
		Document doc = Jsoup.connect(url).get();
		int totalPage = Integer.valueOf(doc.select("totalPage").text());
		int targetPageInt = Integer.valueOf(targetPage);
		if( targetPageInt<0 || targetPageInt>totalPage ){
			throw new ServiceException("目标页数超出范围!");
		}
		//3.拼装URL并连接
		//http://zjldrk.zjsgat.gov.cn:80/tpxw/index.htm?app=1
		String htmVal = "index.htm";
		if(Integer.valueOf(targetPage)>1){
			htmVal = "index_"+targetPageInt+".htm";
		}
		url = ResourceUtils.getProperty("source_web_url")+"/"+articleTypeEnum.getTypeValue()+"/"+htmVal+"?app="+articleTypeEnum.getTypeCode();
		logger.info(">>>类别文章信息集合URL："+url);
		doc = Jsoup.connect(url).get();
		//4.拼装类别文章信息集合内容并返回
		ArticleTypeDto articleTypeDto = new ArticleTypeDto();
		articleTypeDto.setArticleType(articleType);
		articleTypeDto.setTotalCount(doc.select("totalCount").text());
		articleTypeDto.setCurrentPage(doc.select("currentPage").text());
		articleTypeDto.setTotalPage(doc.select("totalPage").text());
		List<ArticleInfoDto> articleInfoDtoList = new ArrayList<>();
		ArticleInfoDto articleInfoDto = null;
		Elements els =  doc.select("dataList");
		for (Iterator<Element> it = els.iterator(); it.hasNext();) {
			Element e = (Element) it.next();  
			articleInfoDto = new ArticleInfoDto();
			articleInfoDto.setArticleID(e.select("newsId").text());
			articleInfoDto.setArticleTitle(e.select("newsTitle").text());
			//只有"图片新闻"类别才有图片URL
			if(articleType.equals(ArticleTypeEnum.TPXW_TYPE.getTypeCode())){
				articleInfoDto.setArticlePicURL(e.select("newsImageLink").text());
			}
			articleInfoDto.setArticleDate(e.select("newsDate").text());
			articleInfoDtoList.add(articleInfoDto);
		}
		articleTypeDto.setDataList(articleInfoDtoList);
		return articleTypeDto;
	}

	/**
	 * 
	 * @MethodName getAllTypeArticleList
	 * @Date   2018年5月31日上午9:30:52
	 * @author wzh
	 *
	 * 获取流动人口管理服务网所有类别文章信息集合
	 * @return ArticleAllTypeDto
	 * @throws Exception
	 * 方法操作步骤简述
	 * 1.拼装URL并连接，同时拼装所有类别文章信息集合内容并返回。
	 */
	public ArticleAllTypeDto getAllTypeArticleList()throws Exception{
		//1.拼装URL并连接，同时拼装所有类别文章信息集合内容并返回。
		ArticleAllTypeDto articleAllTypeDto = new ArticleAllTypeDto();
		
		LinkedHashMap<String, String> articleTypeMap = ArticleTypeEnum.getMap();
		Iterator<Entry<String, String>> iterator= articleTypeMap.entrySet().iterator();  
		  
		String url = "";
		Document doc = null;
		List<ArticleTypeDto> typeList = new ArrayList<ArticleTypeDto>();
		ArticleTypeDto articleTypeDto = null;
		List<ArticleInfoDto> dataList = null;
		ArticleInfoDto articleInfoDto = null;
		while(iterator.hasNext()){
		    Entry entry = iterator.next();
			url = ResourceUtils.getProperty("source_web_url")+"/?app="+entry.getKey();
			logger.info(">>>类别文章信息集合URL："+url);
			doc = Jsoup.connect(url).get();
			
			articleTypeDto = new ArticleTypeDto();
			articleTypeDto.setArticleType((String)entry.getKey());
			dataList = new ArrayList<ArticleInfoDto>();
			Elements els =  doc.select("dataList");
			for (Iterator<Element> it = els.iterator(); it.hasNext();) {
				Element e = (Element) it.next(); 
				articleInfoDto = new ArticleInfoDto();
				articleInfoDto.setArticleID(e.select("newsId").text());
				articleInfoDto.setArticleTitle(e.select("newsTitle").text());
				//只有"图片新闻"类别才有图片URL
				if(ArticleTypeEnum.TPXW_TYPE.getTypeCode().equals((String)entry.getKey())){
					articleInfoDto.setArticlePicURL(e.select("newsImageLink").text());
				}
				articleInfoDto.setArticleDate(e.select("newsDate").text());
				dataList.add(articleInfoDto);
			}
			articleTypeDto.setDataList(dataList);
			typeList.add(articleTypeDto);
		}  
		articleAllTypeDto.setTypeList(typeList);
		return articleAllTypeDto;
	}
}
