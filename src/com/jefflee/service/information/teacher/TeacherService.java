package com.jefflee.service.information.teacher;

import java.util.List;

import com.jefflee.dto.information.TeacherDto;

public interface TeacherService {

	public String create(TeacherDto teacherDto);

	public List<TeacherDto> findAll();

	public TeacherDto findById(String teacherId);

	public String modify(TeacherDto teacherDto);

	public String delete(String teacherId);

}
