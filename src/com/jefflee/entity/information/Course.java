package com.jefflee.entity.information;

import com.jefflee.po.information.CoursePo;

public class Course {
	public Integer courseId;
	public String courseNo;

	public Course() {
	}

	public Course(Integer courseId) {
		this.courseId = courseId;
	}

	public Course(CoursePo coursePo) {
		courseId = coursePo.getCourseId();
		courseNo = coursePo.getCourseNo();
	}

	public CoursePo toPo() {
		CoursePo coursePo = new CoursePo();
		coursePo.setCourseId(courseId);
		coursePo.setCourseNo(courseNo);
		return coursePo;
	}
}
