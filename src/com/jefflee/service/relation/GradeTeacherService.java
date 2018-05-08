package com.jefflee.service.relation;

import java.util.List;

import com.jefflee.entity.relation.GradeTeacher;

public interface GradeTeacherService {

	Integer insert(GradeTeacher gradeTeacher);

	List<GradeTeacher> selectListByGradeId(Integer gradeId);

	void deleteByGradeId(Integer gradeId);

	void delete(GradeTeacher gradeTeacher);

}
