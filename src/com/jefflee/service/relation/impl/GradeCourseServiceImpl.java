package com.jefflee.service.relation.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.relation.GradeCourse;
import com.jefflee.mapper.relation.GradeCourseMapper;
import com.jefflee.service.relation.GradeCourseService;

@Service("gradeCourseService")
public class GradeCourseServiceImpl implements GradeCourseService {

	@Resource(name = "gradeCourseMapper")
	private GradeCourseMapper gradeCourseMapper;

	@Override
	public Integer insert(GradeCourse gradeCourse) {
		if (gradeCourseMapper.insert(gradeCourse) == 1) {
			return gradeCourse.getGradeId();
		} else {
			return null;
		}
	}

	@Override
	public List<GradeCourse> selectByGradeId(Integer gradeId) {
		GradeCourse gradeCourse = new GradeCourse();
		gradeCourse.setGradeId(gradeId);
		return gradeCourseMapper.select(gradeCourse);
	}

	@Override
	public void deleteByGradeId(Integer gradeId) {
		GradeCourse gradeCourse = new GradeCourse();
		gradeCourse.setGradeId(gradeId);
		gradeCourseMapper.delete(gradeCourse);
	}
}
