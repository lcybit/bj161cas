package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.CourseDto;
import com.jefflee.entity.information.Course;
import com.jefflee.mapper.information.CourseMapper;
import com.jefflee.service.information.CourseService;
import com.jefflee.util.BeanUtil;

@Service("courseService")
public class CourseServiceImpl implements CourseService {

	@Resource(name = "courseMapper")
	private CourseMapper courseMapper;

	@Override
	public Integer create(CourseDto courseDto) {
		Course course = new Course();
		BeanUtil.copyProperties(courseDto, course);
		if (courseMapper.insert(course) == 1) {
			return course.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public List<CourseDto> listAll() {
		List<Course> courseList = courseMapper.selectAll();
		List<CourseDto> courseDtoList = new ArrayList<CourseDto>();
		for (Course course : courseList) {
			CourseDto courseDto = new CourseDto();
			BeanUtil.copyPropertiesSelective(course, courseDto);
			courseDtoList.add(courseDto);
		}
		return courseDtoList;
	}

	@Override
	public CourseDto findById(Integer courseId) {
		CourseDto courseDto = new CourseDto();
		Course course = courseMapper.selectByPrimaryKey(courseId);
		BeanUtil.copyProperties(course, courseDto);
		return courseDto;
	}

	@Override
	public Integer modify(CourseDto courseDto) {
		Course course = new Course();
		BeanUtil.copyProperties(courseDto, course);
		if (courseMapper.updateByPrimaryKey(course) == 1) {
			return course.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer courseId) {
		if (courseMapper.deleteByPrimaryKey(courseId) == 1) {
			return courseId;
		} else {
			return null;
		}
	}
}
