package com.eastcompeace.org.article;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Sample {

	public static void main(String[] args) throws Exception {
		String strURL = "http://192.168.7.42:8080/residence/fileserver/service/filectrl/upload?product=zjjzz&project=app";
		Map<String, File> map = new HashMap<String, File>();
		//map.put("file1", new File("D:\\rcpos_v1.6.4.apk"));
		//map.put("file2", new File("D:\\fs-service.log"));
		map.put("file3", new File("D:\\2.jpg"));
		map.put("file4", new File("D:\\3.jpg"));
		
		String tag = "file5";
		File file = new File("D:\\3.jpg");
		
		//String str = httpUpload(strURL, tag, file);
		String str = httpUpload(strURL, map);
		System.out.println(str);
	}
	
	// 单个文件上传
	static String httpUpload(String strURL, String tag, File file) throws Exception {
		Map<String, File> map = new HashMap<String, File>();
		map.put(tag, file);
		
		return httpUpload(strURL, map);
	}
	
	// 多文件同时上传
		static String httpUpload(String strURL, Map<String, File> map) throws Exception {
			final String boundaryPrefix = "--";
			final String boundary = "***d2a7fbc738cec380ee4ea0f1e54c07f8";
			final String newLine = "\r\n";
			
			URL url = new URL(strURL);
			
			// 设置http请求头参数
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			conn.connect();
			
			if (map == null) throw new Exception("要上传的文件为空！");
			
			// 定义输出流对象
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			
			// 构建文件上传头参数
			for (Map.Entry<String, File> m: map.entrySet()) {
				String fieldName = m.getKey();
				File file = m.getValue();
				String fileName = file.getAbsolutePath();
				
				StringBuffer sb = new StringBuffer();
				sb.append(boundaryPrefix + boundary + newLine);
				sb.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + newLine);
				sb.append("Content-Type: application/octet-stream" + newLine);
				sb.append("Content-Transfer-Encoding: binary" + newLine);
				sb.append(newLine);
				
				// 向文件服务器写入文件上传头参数
				dos.writeBytes(sb.toString());
				
				// 向文件服务器写入文件的字节流
				FileInputStream fis = new FileInputStream(fileName);
				byte[] buffer = new byte[1024];
				int length = -1;
				while ((length = fis.read(buffer)) != -1) {
					dos.write(buffer, 0, length);
				}
				
				// 文件字节流写入完成后必须加上换行
				dos.writeBytes(newLine);
				
				dos.flush();
				fis.close();
			}
			
			// 向文件服务器写入baundary结束标记
			dos.writeBytes(newLine + boundaryPrefix + boundary + boundaryPrefix + newLine);
			
			// 从文件服务器取响应
			byte[] data = new byte[0];
			InputStream is = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				byte[] arrBytes = new byte[data.length + length];
					
				System.arraycopy(data, 0, arrBytes, 0, data.length);
				System.arraycopy(buffer, 0, arrBytes, data.length, length);
				data = arrBytes;
			}
			
			// 关闭流
			dos.close();
			is.close();
			
			return new String(data, "UTF-8");
		}
	
}
