package com.eastcompeace.base;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;


/**
 * TimerTask 定时任务
 * 描述：定时每天早上9点启动定时任务函数，：
 * @author ecp-zhaoxi.zeng  2014-6-27
 */
public class TimerTask {
	private static Log LOGGER = LogFactory.getLog(TimerTask.class);
	
	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception{
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void waringNotice() throws Exception{
		
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TimerTask tt = new TimerTask();
		tt.execute();
	}

}
