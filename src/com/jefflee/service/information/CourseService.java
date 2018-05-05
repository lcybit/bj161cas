package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Course;

public interface CourseService {

	Integer insert(Course course);

	List<Course> selectList();

	Course selectById(Integer courseId);

	Integer updateById(Course course);

	Integer deleteById(Integer courseId);

	List<Course> selectListByGradeId(Integer gradeId);

}
