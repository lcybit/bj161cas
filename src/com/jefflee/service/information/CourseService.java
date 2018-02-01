package com.jefflee.service.information;

import java.util.List;

import com.jefflee.po.information.CoursePo;

public interface CourseService {

	public Integer insert(CoursePo coursePo);

	public List<CoursePo> selectAll();

	public CoursePo selectById(Integer courseId);

	public Integer updateById(CoursePo coursePo);

	public Integer deleteById(Integer courseId);

}
