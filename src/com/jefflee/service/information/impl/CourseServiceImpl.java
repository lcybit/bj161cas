package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.mapper.information.CourseMapper;
import com.jefflee.service.information.CourseService;

@Service("courseService")
public class CourseServiceImpl implements CourseService {

	@Resource(name = "courseMapper")
	private CourseMapper courseMapper;

	@Override
	public Integer insert(Course course) {
		if (courseMapper.insert(course) == 1) {
			return course.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public List<Course> selectList() {
		return courseMapper.selectAll();
	}

	@Override
	public Course selectById(Integer courseId) {
		return courseMapper.selectByPrimaryKey(courseId);
	}

	@Override
	public List<Course> selectListByGradeId(Integer gradeId) {
		return courseMapper.selectListByGradeId(gradeId);
	}

	@Override
	public Integer updateById(Course course) {
		if (courseMapper.updateByPrimaryKey(course) == 1) {
			return course.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer courseId) {
		if (courseMapper.deleteByPrimaryKey(courseId) == 1) {
			return courseId;
		} else {
			return null;
		}
	}

}