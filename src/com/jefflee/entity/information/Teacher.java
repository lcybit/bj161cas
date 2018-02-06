package com.jefflee.entity.information;

import com.jefflee.po.information.TeacherPo;

public class Teacher {
	public Integer teacherId;
	public String teacherNo;
	public String name;
	public Integer type;

	public Teacher() {
	}

	public Teacher(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public Teacher(TeacherPo teacherPo) {
		teacherId = teacherPo.getTeacherId();
		teacherNo = teacherPo.getTeacherNo();
		name = teacherPo.getName();
		type = teacherPo.getType();
	}

	public TeacherPo toPo() {
		TeacherPo teacherPo = new TeacherPo();
		teacherPo.setTeacherId(teacherId);
		teacherPo.setTeacherNo(teacherNo);
		teacherPo.setName(name);
		teacherPo.setType(type);
		return teacherPo;
	}
}
