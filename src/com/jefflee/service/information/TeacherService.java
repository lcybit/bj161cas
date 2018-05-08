package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Teacher;

public interface TeacherService {

	Integer insert(Teacher teacher);

	List<Teacher> selectList();

	Teacher selectById(Integer teacherId);

	Integer updateById(Teacher teacher);

	Integer deleteById(Integer teacherId);

	List<Teacher> selectListByGradeId(Integer scheduleId);

}
