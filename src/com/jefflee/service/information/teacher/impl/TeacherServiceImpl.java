package com.jefflee.service.information.teacher.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.TeacherDto;
import com.jefflee.entity.information.Teacher;
import com.jefflee.mapper.information.TeacherMapper;
import com.jefflee.service.information.teacher.TeacherService;
import com.jefflee.util.BeanUtil;
import com.jefflee.util.DatabaseUtil;

import tk.mybatis.mapper.entity.Example;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

	@Resource(name = "teacherMapper")
	private TeacherMapper teacherMapper;

	@Override
	public String create(TeacherDto teacherDto) {
		String teacherId = DatabaseUtil.gnr32Uuid();
		teacherDto.setTeacherId(teacherId);
		Teacher teacher = new Teacher();
		BeanUtil.copyProperties(teacherDto, teacher);
		teacherMapper.insert(teacher);
		return teacherId;
	}

	@Override
	public List<TeacherDto> findAll() {
		Example example = new Example(Teacher.class);
		example.setOrderByClause("teacher_no ASC");
		List<Teacher> teacherList = teacherMapper.selectByExample(example);
		List<TeacherDto> teacherDtoList = new ArrayList<TeacherDto>();
		for (Teacher teacher : teacherList) {
			TeacherDto teacherDto = new TeacherDto();
			BeanUtil.copyPropertiesSelective(teacher, teacherDto);
			teacherDtoList.add(teacherDto);
		}
		return teacherDtoList;
	}

	@Override
	public TeacherDto findById(String teacherId) {
		TeacherDto teacherDto = new TeacherDto();
		Teacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
		BeanUtil.copyProperties(teacher, teacherDto);
		return teacherDto;
	}

	@Override
	public String modify(TeacherDto teacherDto) {
		Teacher teacher = new Teacher();
		BeanUtil.copyProperties(teacherDto, teacher);
		teacherMapper.updateByPrimaryKey(teacher);
		return teacher.getTeacherId();
	}

	@Override
	public String delete(String teacherId) {
		teacherMapper.deleteByPrimaryKey(teacherId);
		return teacherId;
	}
}
