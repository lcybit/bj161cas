package com.jefflee.service.schedule;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.jefflee.po.schedule.SchedulePo;
import com.jefflee.view.SchdPlanView;
import com.jefflee.view.ScheduleView;

public interface ScheduleService {

	Integer insert(SchedulePo schedulePo);

	List<SchedulePo> selectAll();

	SchedulePo selectById(Integer scheduleId);

	Integer updateById(SchedulePo schedulePo);

	Integer deleteById(Integer scheduleId);

	ScheduleView gnrScheduleView(Integer scheduleId);

	SchdPlanView gnrSchdPlanView(Integer groupId, Integer scheduleId);

	void gnrEmptyArrangementList(Integer scheduleId);

	void gnrSchedule(Integer scheduleId);

	void gnrScheduleViewExcel(HttpServletResponse response, Integer scheduleId) throws Exception;

	void initial(Integer scheduleId);

	List<SchedulePo> selectListByGroupId(Integer groupId);
}
