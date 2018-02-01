package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Day;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.po.information.CoursePo;
import com.jefflee.po.information.TclassPo;
import com.jefflee.po.schedule.SchedulePo;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.view.CourseView;
import com.jefflee.view.ScheduleView;
import com.jefflee.view.WeekView;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;

	@Resource(name = "planService")
	private PlanService planService;
	@Resource(name = "arrangementService")
	private ArrangementService arrangementService;

	@Resource(name = "courseService")
	private CourseService courseService;
	@Resource(name = "tclassService")
	private TclassService tclassService;

	@Override
	public Integer insert(SchedulePo schedulePo) {
		if (scheduleMapper.insert(schedulePo) == 1) {
			return schedulePo.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public List<SchedulePo> selectAll() {
		return scheduleMapper.selectAll();
	}

	@Override
	public SchedulePo selectById(Integer scheduleId) {
		return scheduleMapper.selectByPrimaryKey(scheduleId);
	}

	@Override
	public Integer updateById(SchedulePo schedulePo) {
		if (scheduleMapper.updateByPrimaryKey(schedulePo) == 1) {
			return schedulePo.getScheduleId();
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

	@Override
	public ScheduleView gnrScheduleView(Integer scheduleId) {
		ScheduleView scheduleView = new ScheduleView();

		Schedule schedule = new Schedule(scheduleMapper.selectByPrimaryKey(scheduleId));
		scheduleView.setSchedule(schedule);

		List<Course> courseList = new ArrayList<Course>();
		// TODO part of
		List<CoursePo> coursePoList = courseService.selectAll();
		for (CoursePo coursePo : coursePoList) {
			courseList.add(new Course(coursePo));
		}
		scheduleView.setCourseList(courseList);

		List<Tclass> tclassList = new ArrayList<Tclass>();
		// TODO part of
		List<TclassPo> tclassPoList = tclassService.selectAll();
		for (TclassPo tclassPo : tclassPoList) {
			tclassList.add(new Tclass(tclassPo));
		}

		List<WeekView> weekViewList = gnrWeekViewList(schedule, courseList, tclassList);
		scheduleView.setWeekViewList(weekViewList);

		return scheduleView;
	}

	private List<WeekView> gnrWeekViewList(Schedule schedule, List<Course> courseList, List<Tclass> tclassList) {
		List<WeekView> weekViewList = new ArrayList<WeekView>();

		for (Tclass tclass : tclassList) {
			weekViewList.add(gnrWeekView(schedule, courseList, tclass));
		}

		return weekViewList;
	}

	private WeekView gnrWeekView(Schedule schedule, List<Course> courseList, Tclass tclass) {
		WeekView weekView = new WeekView();

		weekView.setType("tclass");
		weekView.setTypeId(tclass.tclassId);

		// TODO
		List<Day> dayList = new ArrayList<Day>();
		dayList = arrangementService.gnrDayList(schedule, tclass);
		weekView.setDayList(dayList);

		List<CourseView> courseViewList = new ArrayList<CourseView>();
		for (Course course : courseList) {
			courseViewList.add(planService.gnrCourseView(schedule, course, tclass));
		}
		weekView.setCourseViewList(courseViewList);

		return weekView;
	}
}
