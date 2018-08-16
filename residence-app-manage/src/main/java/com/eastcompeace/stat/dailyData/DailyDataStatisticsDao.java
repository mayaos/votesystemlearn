package com.eastcompeace.stat.dailyData;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.AppDailyDataModel;

@Repository
public interface DailyDataStatisticsDao {
	/**
	 * 查询APP日常统计信息列表
	 * @param addm
	 * @return
	 * @throws Exception
	 */
	 List<AppDailyDataModel> queryForList(AppDailyDataModel addm) throws Exception;

}
