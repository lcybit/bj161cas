package com.jefflee.mapper.relation;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.relation.GradeTeacher;
import com.jefflee.util.MyMapper;

@Repository("gradeTeacherMapper")
public interface GradeTeacherMapper extends MyMapper<GradeTeacher> {

}