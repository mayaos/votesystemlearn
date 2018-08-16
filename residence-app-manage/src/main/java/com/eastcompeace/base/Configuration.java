package com.eastcompeace.base;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 系统配置文件实例对象
 * 
 * @author qinyong 2015-6-25
 */
public class Configuration {

	static final Log LOGGER = LogFactory.getLog(Configuration.class);
	static final String FILE_NAME = "configuration.xml";
	static Configuration config;

	/**
	 * 加载系统配置文件，并返回配置对象的单实例
	 * 
	 * @return
	 */
	@SuppressWarnings("null")
	public static Configuration instance() {
		List<String> list = new ArrayList<String>();
		if (config == null)
			config = new Configuration();

		try {
			InputStream is = Configuration.class.getClassLoader().getResourceAsStream(FILE_NAME);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			SAXReader reader = new SAXReader();
			reader.setEncoding("UTF-8");
			Document doc = reader.read(isr);
			 Element  root =  doc.getRootElement();//根节点
				for (Iterator i = root.elementIterator(); i.hasNext();) {
					Element head = (Element) i.next();
					for (Iterator j = head.elementIterator(); j.hasNext();) {
						Element elem= (Element) j.next();
						list.add(elem.getTextTrim());
					}
				}
				config.setNodes(list);
		} catch (Exception ex) {
			LOGGER.error("加载系统配置文件失败：" + ex);
			ex.printStackTrace();
			System.exit(0);
		}

		return config;
	}

	public static void main(String[] args) {
		InputStream is = Configuration.class.getClassLoader()
				.getResourceAsStream(FILE_NAME);
		InputStreamReader isr = null;
		Document doc = null;
		List list  = null;
		try {
			isr = new InputStreamReader(is, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		try {
			 doc = reader.read(isr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		 
		 Element  root =  doc.getRootElement();
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element head = (Element) i.next();
			for (Iterator j = head.elementIterator(); j.hasNext();) {
				Element elem= (Element) j.next();
				System.out.println(elem.getTextTrim());
			}
		}
	}

	private List nodes;

	public List getNodes() {
		return nodes;
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}

}
