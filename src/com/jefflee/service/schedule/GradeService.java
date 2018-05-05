package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.entity.schedule.Grade;

public interface GradeService {

	Integer insert(Grade grade);

	List<Grade> selectList();

	Grade selectById(Integer gradeId);

	Integer updateById(Grade grade);

	Integer deleteById(Integer gradeId);

}
