package com.jefflee.service.information;

import java.util.List;

import com.jefflee.dto.information.TeacherDto;

public interface TeacherService {

	public Integer create(TeacherDto teacherDto);

	public List<TeacherDto> listAll();

	public TeacherDto findById(Integer teacherId);

	public Integer modify(TeacherDto teacherDto);

	public Integer delete(Integer teacherId);

}
