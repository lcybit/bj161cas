package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.TeacherDto;
import com.jefflee.entity.information.Teacher;
import com.jefflee.mapper.information.TeacherMapper;
import com.jefflee.service.information.TeacherService;
import com.jefflee.util.BeanUtil;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

	@Resource(name = "teacherMapper")
	private TeacherMapper teacherMapper;

	@Override
	public Integer create(TeacherDto teacherDto) {
		Teacher teacher = new Teacher();
		BeanUtil.copyProperties(teacherDto, teacher);
		if (teacherMapper.insert(teacher) == 1) {
			return teacher.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public List<TeacherDto> listAll() {
		List<Teacher> teacherList = teacherMapper.selectAll();
		List<TeacherDto> teacherDtoList = new ArrayList<TeacherDto>();
		for (Teacher teacher : teacherList) {
			TeacherDto teacherDto = new TeacherDto();
			BeanUtil.copyPropertiesSelective(teacher, teacherDto);
			teacherDtoList.add(teacherDto);
		}
		return teacherDtoList;
	}

	@Override
	public TeacherDto findById(Integer teacherId) {
		TeacherDto teacherDto = new TeacherDto();
		Teacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
		BeanUtil.copyProperties(teacher, teacherDto);
		return teacherDto;
	}

	@Override
	public Integer modify(TeacherDto teacherDto) {
		Teacher teacher = new Teacher();
		BeanUtil.copyProperties(teacherDto, teacher);
		if (teacherMapper.updateByPrimaryKey(teacher) == 1) {
			return teacher.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer teacherId) {
		if (teacherMapper.deleteByPrimaryKey(teacherId) == 1) {
			return teacherId;
		} else {
			return null;
		}
	}
}
