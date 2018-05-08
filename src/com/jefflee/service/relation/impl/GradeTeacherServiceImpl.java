package com.jefflee.service.relation.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.relation.GradeTeacher;
import com.jefflee.mapper.relation.GradeTeacherMapper;
import com.jefflee.service.relation.GradeTeacherService;

@Service("gradeTeacherService")
public class GradeTeacherServiceImpl implements GradeTeacherService {

	@Resource(name = "gradeTeacherMapper")
	private GradeTeacherMapper gradeTeacherMapper;

	@Override
	public Integer insert(GradeTeacher gradeTeacher) {
		if (gradeTeacherMapper.insert(gradeTeacher) == 1) {
			return gradeTeacher.getGradeId();
		} else {
			return null;
		}
	}

	@Override
	public List<GradeTeacher> selectListByGradeId(Integer gradeId) {
		GradeTeacher gradeTeacher = new GradeTeacher();
		gradeTeacher.setGradeId(gradeId);
		return gradeTeacherMapper.select(gradeTeacher);
	}

	@Override
	public void deleteByGradeId(Integer gradeId) {
		GradeTeacher gradeTeacher = new GradeTeacher();
		gradeTeacher.setGradeId(gradeId);
		gradeTeacherMapper.delete(gradeTeacher);
	}

	@Override
	public void delete(GradeTeacher gradeTeacher) {
		gradeTeacherMapper.delete(gradeTeacher);
	}
}
