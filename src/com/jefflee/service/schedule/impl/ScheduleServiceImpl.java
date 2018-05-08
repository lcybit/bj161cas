package com.jefflee.service.schedule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.GradeService;
import com.jefflee.service.schedule.ScheduleService;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;

	@Resource(name = "periodService")
	private PeriodService periodService;
	@Resource(name = "courseService")
	private CourseService courseService;
	@Resource(name = "tclassService")
	private TclassService tclassService;
	@Resource(name = "teacherService")
	private TeacherService teacherService;

	@Resource(name = "gradeService")
	private GradeService gradeService;

	@Override
	public Integer insert(Schedule schedule) {
		if (scheduleMapper.insert(schedule) == 1) {
			return schedule.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public List<Schedule> selectList() {
		return scheduleMapper.selectDetailList();
	}

	@Override
	public List<Schedule> selectListByGradeId(Integer gradeId) {
		return scheduleMapper.selectDetailListByGradeId(gradeId);
	}

	@Override
	public Schedule selectById(Integer scheduleId) {
		return scheduleMapper.selectDetailById(scheduleId);
	}

	@Override
	public Integer updateById(Schedule schedule) {
		if (scheduleMapper.updateByPrimaryKey(schedule) == 1) {
			return schedule.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer scheduleId) {
		if (scheduleMapper.deleteByPrimaryKey(scheduleId) == 1) {
			return scheduleId;
		} else {
			return null;
		}
	}

}