package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.entity.schedule.Plan;
import com.jefflee.view.SchedulePlanView;

public interface PlanService {

	Integer insert(Plan plan);

	List<Plan> selectList();

	Plan selectById(Integer planId);

	List<Plan> getPlanById(Integer courseId, Integer scheduleId);

	Integer updateById(Plan plan);

	Integer deleteById(Integer planId);

	List<Plan> selectListByScheduleId(Integer scheduleId);

	List<Plan> selectDetailListByScheduleId(Integer scheduleId);

	void copyListByScheduleId(Integer srcScheduleId, Integer destScheduleId);

	SchedulePlanView gnrSchedulePlanView(Integer scheduleId);

	void gnrEmptyPlanList(Integer scheduleId);

}
