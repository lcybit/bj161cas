package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.entity.schedule.Adjustment;

public interface AdjustmentService {

	Integer insert(Adjustment adjustment);

	Adjustment selectById(Integer adjustmentId);

	List<Adjustment> selectList();

	int updateById(Adjustment adjustment);

	int deleteById(Integer adjustmentId);

	List<Adjustment> selectListByScheduleId(Integer scheduleId);

	List<Adjustment> selectTempListByScheduleId(Integer scheduleId);

	Adjustment selectLatest(Integer scheduleId);

}
