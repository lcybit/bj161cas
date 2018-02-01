package com.jefflee.view;

import java.util.List;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.schedule.Schedule;

public class ScheduleView {
	private Schedule schedule;
	private List<Course> courseList;
	private List<WeekView> weekViewList;

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public List<WeekView> getWeekViewList() {
		return weekViewList;
	}

	public void setWeekViewList(List<WeekView> weekViewList) {
		this.weekViewList = weekViewList;
	}
}
