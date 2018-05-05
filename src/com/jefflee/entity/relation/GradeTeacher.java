package com.jefflee.entity.relation;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "rlat_grade_teacher")
public class GradeTeacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer gradeTeacherId;
	public Integer gradeId;
	public Integer teacherId;

	public Integer getGradeTeacherId() {
		return gradeTeacherId;
	}

	public void setGradeTeacherId(Integer gradeTeacherId) {
		this.gradeTeacherId = gradeTeacherId;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

}
