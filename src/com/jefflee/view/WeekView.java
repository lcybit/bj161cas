package com.jefflee.view;

import java.util.List;

public class WeekView {
	private String type;
	private Integer typeId;
	private List<DayView> dayViewList;
	private List<CourseView> courseViewList;

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

	public List<DayView> getDayViewList() {
		return dayViewList;
	}

	public void setDayViewList(List<DayView> dayViewList) {
		this.dayViewList = dayViewList;
	}

}
