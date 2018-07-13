package com.eastcompeace.test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

/**
 * 接口测试
 */
public class InterfaceTest {
	
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	 
	public static void main(String[] args) {
		//获取所有类别文章信息集合接口
		getAllTypeArticleList();
		//获取指定类别文章信息集合接口
		getTargetTypeArticleList();
		//获取指定文章信息接口
		getTargetArticleContent();
		
		//下载文件
//		downloadFile();
		
		//意见建议信息集合接口
		getSuggestList();
		//提交意见建议信息接口
//		submitSuggest();
		
	}
	
	
	public static void getAllTypeArticleList(){
		//获取所有类别文章信息集合接口 - start
		JSONObject bodyObj = new JSONObject();
		try {
			//开发环境
//			String returnStr = connect("http://localhost:8084/residence/app/population/articleCtrl/getAllTypeArticleList",bodyObj.toString(),"POST");
			//本机测试环境
//			String returnStr = connect("http://192.168.5.102:8088/residence/app/population/articleCtrl/getAllTypeArticleList",bodyObj.toString(),"POST");
			//公司测试环境
			String returnStr = connect("http://zjjzz.eastcompeace.com/test-rc/population/articleCtrl/getAllTypeArticleList",bodyObj.toString(),"POST");
			//公司正式环境
//			String returnStr = connect("http://jzz.gxdata.net/residence/app/population/articleCtrl/getAllTypeArticleList",bodyObj.toString(),"POST");
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取所有类别文章信息集合接口 - end
	}
	
	
	public static void getTargetTypeArticleList(){
		//获取指定类别文章信息集合接口 - start
		JSONObject bodyObj = new JSONObject();
		bodyObj.put("articleType","1");
		bodyObj.put("targetPage","1");
		try {
			//开发环境
//			String returnStr = connect("http://localhost:8084/residence/app/population/articleCtrl/getTargetTypeArticleList",bodyObj.toString(),"POST");
		    //本机测试环境
//			String returnStr = connect("http://192.168.5.102:8088/residence/app/population/articleCtrl/getTargetTypeArticleList",bodyObj.toString(),"POST");
			//公司测试环境
			String returnStr = connect("http://zjjzz.eastcompeace.com/test-rc/population/articleCtrl/getTargetTypeArticleList",bodyObj.toString(),"POST");
			//公司正式环境
//			String returnStr = connect("http://jzz.gxdata.net/residence/app/population/articleCtrl/getTargetTypeArticleList",bodyObj.toString(),"POST");
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取指定类别文章信息集合接口 - end
	}
	
	public static void getTargetArticleContent(){
		//获取指定文章信息接口 - start
		JSONObject bodyObj = new JSONObject();
		bodyObj.put("articleType","1");
		bodyObj.put("articleID","4431");
		try {
		    //开发环境
//			String returnStr = connect("http://localhost:8084/residence/app/population/articleCtrl/getTargetArticleContent",bodyObj.toString(),"POST");
		    //本机测试环境
//			String returnStr = connect("http://192.168.5.102:8088/residence/app/population/articleCtrl/getTargetArticleContent",bodyObj.toString(),"POST");
			//公司测试环境
			String returnStr = connect("http://zjjzz.eastcompeace.com/test-rc/population/articleCtrl/getTargetArticleContent",bodyObj.toString(),"POST");
			//公司正式环境
//			String returnStr = connect("http://jzz.gxdata.net/residence/app/population/articleCtrl/getTargetArticleContent",bodyObj.toString(),"POST");
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取指定文章信息接口 - end
	}
	
	public static void downloadFile(){
		//下载文件 - start
		JSONObject bodyObj = new JSONObject();
		bodyObj.put("filePath","/static/files/hangzhou.pdf");
		try {
		    //开发环境
//			String returnStr = connect("http://localhost:8084/residence/app/population/articleCtrl/downloadFile",bodyObj.toString(),"POST");
		    //本机测试环境
//			String returnStr = connect("http://192.168.5.102:8088/residence/app/population/articleCtrl/downloadFile",bodyObj.toString(),"POST");
			//公司测试环境
			String returnStr = connect("http://zjjzz.eastcompeace.com/test-rc/population/articleCtrl/downloadFile",bodyObj.toString(),"POST");
			//公司正式环境
//			String returnStr = connect("http://jzz.gxdata.net/residence/app/population/articleCtrl/downloadFile",bodyObj.toString(),"POST");
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//下载文件 - end
	}
	
	public static void getSuggestList(){
		//意见建议信息集合接口 - start
		JSONObject bodyObj = new JSONObject();
		bodyObj.put("targetPage","2");
		try {
			//开发环境
//			String returnStr = connect("http://localhost:8084/residence/app/population/suggestCtrl/getSuggestList",bodyObj.toString(),"POST");
		    //本机测试环境
//			String returnStr = connect("http://192.168.5.102:8088/residence/app/population/suggestCtrl/getSuggestList",bodyObj.toString(),"POST");
			//公司测试环境
			String returnStr = connect("http://zjjzz.eastcompeace.com/test-rc/population/suggestCtrl/getSuggestList",bodyObj.toString(),"POST");
			//公司正式环境
//			String returnStr = connect("http://jzz.gxdata.net/residence/app/population/suggestCtrl/getSuggestList",bodyObj.toString(),"POST");
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//意见建议信息集合接口 - end
	}
	
	public static void submitSuggest(){
		//提交意见建议信息接口 - start
		JSONObject bodyObj = new JSONObject();
		bodyObj.put("ctgId","1");
		bodyObj.put("email","469635893@qq.com");
		bodyObj.put("phone","13543099777");
		bodyObj.put("qq","469635893");
		bodyObj.put("title","办理浙江省居住证需要什么材料信息？");
		bodyObj.put("content","办理浙江省居住证需要什么材料信息？");
		bodyObj.put("captcha","ta12");
		try {
			//开发环境
//			String returnStr = connect("http://localhost:8084/residence/app/population/suggestCtrl/submitSuggest",bodyObj.toString(),"POST");
		    //本机测试环境
//			String returnStr = connect("http://192.168.5.102:8088/residence/app/population/suggestCtrl/submitSuggest",bodyObj.toString(),"POST");
			//公司测试环境
			String returnStr = connect("http://zjjzz.eastcompeace.com/test-rc/population/suggestCtrl/submitSuggest",bodyObj.toString(),"POST");
			//公司正式环境
//			String returnStr = connect("http://jzz.gxdata.net/residence/app/population/suggestCtrl/submitSuggest",bodyObj.toString(),"POST");
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//提交意见建议信息接口 - end
	}
	
	
	 /**
    *
    * @param strUrl 请求地址
    * @param params 请求参数
    * @param method 请求方法类型 POST GET
    * @return  网络请求字符串
    * @throws Exception
    */
   public static String connect(String strUrl,String params,String method) throws Exception {
	   
       HttpURLConnection conn = null;
       BufferedReader reader = null;
       String rs = null;
       ByteArrayOutputStream baos = null;
       try {
    	   URL url = new URL(strUrl);
    	   conn = (HttpURLConnection) url.openConnection();
    	   if(method.toUpperCase().equals("POST")){
	           StringBuffer sb = new StringBuffer();
	           conn.setRequestMethod("POST");
	           conn.setDoOutput(true);
	           conn.setRequestProperty("User-agent", userAgent);
	           conn.setUseCaches(false);
	           conn.setConnectTimeout(DEF_CONN_TIMEOUT);
	           conn.setReadTimeout(DEF_READ_TIMEOUT);
	           conn.setInstanceFollowRedirects(false);
	           conn.connect();
	           if (params!= null && method.toUpperCase().equals("POST")) {
	               try {
	                   DataOutputStream out = new DataOutputStream(conn.getOutputStream());
	                   out.write(params.getBytes(DEF_CHATSET));
	               } catch (Exception e) {
	            	   e.printStackTrace();
	               }
	           }
	           InputStream is = conn.getInputStream();
	           reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
	           String strRead = null;
	           while ((strRead = reader.readLine()) != null) {
	               sb.append(strRead);
	           }
	           rs = sb.toString();
    	   }else if(method.toUpperCase().equals("GET")){
               if(200 == conn.getResponseCode()){
                   //得到输入流
                   InputStream is =conn.getInputStream();
                   baos = new ByteArrayOutputStream();
                   byte[] buffer = new byte[1024];
                   int len = 0;
                   while(-1 != (len = is.read(buffer))){
                       baos.write(buffer,0,len);
                       baos.flush();
                   }
                   return baos.toString("utf-8");
               }
    	   }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (reader != null) {
               reader.close();
           }
           if (conn != null) {
               conn.disconnect();
           }
           if (baos != null){
        	   baos.close(); 
           }
       }
       return rs;
   }
   
	public static String dateToString(Date date, String format) {
		String result = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			result = formater.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}
   
}
