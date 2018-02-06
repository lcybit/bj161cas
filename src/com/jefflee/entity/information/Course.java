package com.jefflee.entity.information;

import com.jefflee.po.information.CoursePo;

public class Course {
	public Integer courseId;
	public String courseNo;
	public String name;
	public Integer type;

	public Course() {
	}

	public Course(Integer courseId) {
		this.courseId = courseId;
	}

	public Course(CoursePo coursePo) {
		courseId = coursePo.getCourseId();
		courseNo = coursePo.getCourseNo();
		name = coursePo.getName();
		type = coursePo.getType();
	}

	public CoursePo toPo() {
		CoursePo coursePo = new CoursePo();
		coursePo.setCourseId(courseId);
		coursePo.setCourseNo(courseNo);
		coursePo.setName(name);
		coursePo.setType(type);
		return coursePo;
	}
}
