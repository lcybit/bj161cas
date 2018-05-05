package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Teacher;
import com.jefflee.mapper.information.TeacherMapper;
import com.jefflee.service.information.TeacherService;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

	@Resource(name = "teacherMapper")
	private TeacherMapper teacherMapper;

	@Override
	public Integer insert(Teacher teacher) {
		if (teacherMapper.insert(teacher) == 1) {
			return teacher.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public List<Teacher> selectList() {
		return teacherMapper.selectAll();
	}

	@Override
	public Teacher selectById(Integer teacherId) {
		return teacherMapper.selectByPrimaryKey(teacherId);
	}

	@Override
	public Integer updateById(Teacher teacher) {
		if (teacherMapper.updateByPrimaryKey(teacher) == 1) {
			return teacher.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer teacherId) {
		if (teacherMapper.deleteByPrimaryKey(teacherId) == 1) {
			return teacherId;
		} else {
			return null;
		}
	}

	@Override
	public List<Teacher> selectListByScheduleId(Integer scheduleId) {
		return teacherMapper.selectListByScheduleId(scheduleId);
	}

}
