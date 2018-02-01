package com.jefflee.service.information;

import java.util.List;

import com.jefflee.po.information.TeacherPo;

public interface TeacherService {

	public Integer insert(TeacherPo teacherPo);

	public List<TeacherPo> selectAll();

	public TeacherPo selectById(Integer teacherId);

	public Integer updateById(TeacherPo teacherPo);

	public Integer deleteById(Integer teacherId);

}
