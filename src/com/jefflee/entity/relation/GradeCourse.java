package com.jefflee.entity.relation;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "rlat_grade_course")
public class GradeCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer gradeCourseId;
	public Integer gradeId;
	public Integer courseId;

	public Integer getGradeCourseId() {
		return gradeCourseId;
	}

	public void setGradeCourseId(Integer gradeCourseId) {
		this.gradeCourseId = gradeCourseId;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

}
