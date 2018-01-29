package com.jefflee.entity.information;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_teacher")
public class Teacher {
	@Id
	@Column(name = "teacher_id")
	private String teacherId;

	@Column(name = "teacher_no")
	private String teacherNo;

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherNo() {
		return teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}
}
