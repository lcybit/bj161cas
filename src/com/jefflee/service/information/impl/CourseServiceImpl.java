package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.CourseDto;
import com.jefflee.mapper.information.CourseMapper;
import com.jefflee.po.information.CoursePo;
import com.jefflee.service.information.CourseService;
import com.jefflee.util.BeanUtil;

@Service("courseService")
public class CourseServiceImpl implements CourseService {

	@Resource(name = "courseMapper")
	private CourseMapper courseMapper;

	@Override
	public Integer create(CourseDto courseDto) {
		CoursePo coursePo = new CoursePo();
		BeanUtil.copyProperties(courseDto, coursePo);
		if (courseMapper.insert(coursePo) == 1) {
			return coursePo.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public List<CourseDto> listAll() {
		List<CoursePo> coursePoList = courseMapper.selectAll();
		List<CourseDto> courseDtoList = new ArrayList<CourseDto>();
		for (CoursePo coursePo : coursePoList) {
			CourseDto courseDto = new CourseDto();
			BeanUtil.copyPropertiesSelective(coursePo, courseDto);
			courseDtoList.add(courseDto);
		}
		return courseDtoList;
	}

	@Override
	public CourseDto findById(Integer courseId) {
		CourseDto courseDto = new CourseDto();
		CoursePo coursePo = courseMapper.selectByPrimaryKey(courseId);
		BeanUtil.copyProperties(coursePo, courseDto);
		return courseDto;
	}

	@Override
	public Integer modify(CourseDto courseDto) {
		CoursePo coursePo = new CoursePo();
		BeanUtil.copyProperties(courseDto, coursePo);
		if (courseMapper.updateByPrimaryKey(coursePo) == 1) {
			return coursePo.getCourseId();
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
