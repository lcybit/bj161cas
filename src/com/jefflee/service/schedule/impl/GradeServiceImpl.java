package com.jefflee.service.schedule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.schedule.Grade;
import com.jefflee.mapper.schedule.GradeMapper;
import com.jefflee.service.relation.GradeCourseService;
import com.jefflee.service.relation.GradeTeacherService;
import com.jefflee.service.schedule.GradeService;

@Service("gradeService")
public class GradeServiceImpl implements GradeService {

	@Resource(name = "gradeMapper")
	private GradeMapper gradeMapper;

	@Resource(name = "gradeTeacherService")
	private GradeTeacherService gradeTeacherService;
	@Resource(name = "gradeCourseService")
	private GradeCourseService gradeCourseService;

	@Override
	public Integer insert(Grade grade) {
		if (gradeMapper.insert(grade) == 1) {
			return grade.getGradeId();
		} else {
			return null;
		}
	}

	@Override
	public List<Grade> selectList() {
		return gradeMapper.selectAll();
	}

	@Override
	public Grade selectById(Integer gradeId) {
		return gradeMapper.selectByPrimaryKey(gradeId);
	}

	@Override
	public Integer updateById(Grade grade) {
		if (gradeMapper.updateByPrimaryKey(grade) == 1) {
			return grade.getGradeId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer gradeId) {
		gradeCourseService.deleteByGradeId(gradeId);
		gradeTeacherService.deleteByGradeId(gradeId);
		if (gradeMapper.deleteByPrimaryKey(gradeId) == 1) {
			return gradeId;
		} else {
			return null;
		}
	}

}
