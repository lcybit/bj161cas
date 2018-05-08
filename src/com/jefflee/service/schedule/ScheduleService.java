package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.entity.schedule.Schedule;

public interface ScheduleService {

	Integer insert(Schedule schedule);

	List<Schedule> selectList();

	Schedule selectById(Integer scheduleId);

	Integer updateById(Schedule schedule);

	Integer deleteById(Integer scheduleId);

	List<Schedule> selectListByGradeId(Integer gradeId);
}
