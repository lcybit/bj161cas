package com.jefflee.service.relation;

import java.util.List;

import com.jefflee.entity.relation.GradeTeacher;

public interface GradeTeacherService {

	public Integer insert(GradeTeacher gradeTeacher);

	public List<GradeTeacher> selectByGradeId(Integer gradeId);

	public void deleteByGradeId(Integer gradeId);

}
