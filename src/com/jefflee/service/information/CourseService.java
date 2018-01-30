package com.jefflee.service.information;

import java.util.List;

import com.jefflee.dto.information.CourseDto;

public interface CourseService {

	public Integer create(CourseDto courseDto);

	public List<CourseDto> listAll();

	public CourseDto findById(Integer courseId);

	public Integer modify(CourseDto courseDto);

	public Integer delete(Integer courseId);

}
