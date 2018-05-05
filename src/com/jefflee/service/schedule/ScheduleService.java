package com.jefflee.service.schedule;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.jefflee.entity.schedule.Schedule;
import com.jefflee.view.ScheduleView;

public interface ScheduleService {

	Integer insert(Schedule schedule);

	List<Schedule> selectList();

	Schedule selectById(Integer scheduleId);

	Integer updateById(Schedule schedule);

	Integer deleteById(Integer scheduleId);

	ScheduleView gnrScheduleView(Integer scheduleId);

	void gnrEmptyArrangementList(Integer scheduleId);

	void gnrSchedule(Integer scheduleId);

	void exportExcelView(HttpServletResponse response, Integer scheduleId) throws Exception;

	void initial(Integer scheduleId);

	List<Schedule> selectListByGradeId(Integer gradeId);
}
