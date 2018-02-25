package com.jefflee.view;

import java.util.List;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Schedule;

public class ScheduleView {
	private Schedule schedule;
	private List<Course> courseList;
	private List<Tclass> tclassList;
	private List<WeekView> tclassWeekViewList;
	private List<WeekView> teacherWeekViewList;

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

	public List<Tclass> getTclassList() {
		return tclassList;
	}

	public void setTclassList(List<Tclass> tclassList) {
		this.tclassList = tclassList;
	}

	public List<WeekView> getTclassWeekViewList() {
		return tclassWeekViewList;
	}

	public void setTclassWeekViewList(List<WeekView> tclassWeekViewList) {
		this.tclassWeekViewList = tclassWeekViewList;
	}

	public List<WeekView> getTeacherWeekViewList() {
		return teacherWeekViewList;
	}

	public void setTeacherWeekViewList(List<WeekView> teacherWeekViewList) {
		this.teacherWeekViewList = teacherWeekViewList;
	}
}
