package com.jefflee.view;

import com.jefflee.entity.information.Course;

public class CourseView {
	private Course course;
	private Integer periodNum;
	private Integer arrangedNum;

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(Integer periodNum) {
		this.periodNum = periodNum;
	}

	public Integer getArrangedNum() {
		return arrangedNum;
	}

	public void setArrangedNum(Integer arrangedNum) {
		this.arrangedNum = arrangedNum;
	}
}
