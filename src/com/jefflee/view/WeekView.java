package com.jefflee.view;

import java.util.List;

import com.jefflee.entity.schedule.Day;

public class WeekView {
	private String type;
	private Integer typeId;
	private List<Day> dayList;
	private List<CourseView> courseViewList;

	public List<Day> getDayList() {
		return dayList;
	}

	public void setDayList(List<Day> dayList) {
		this.dayList = dayList;
	}

	public List<CourseView> getCourseViewList() {
		return courseViewList;
	}

	public void setCourseViewList(List<CourseView> courseViewList) {
		this.courseViewList = courseViewList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
