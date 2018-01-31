package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.TeacherDto;
import com.jefflee.mapper.information.TeacherMapper;
import com.jefflee.po.information.TeacherPo;
import com.jefflee.service.information.TeacherService;
import com.jefflee.util.BeanUtil;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

	@Resource(name = "teacherMapper")
	private TeacherMapper teacherMapper;

	@Override
	public Integer create(TeacherDto teacherDto) {
		TeacherPo teacherPo = new TeacherPo();
		BeanUtil.copyProperties(teacherDto, teacherPo);
		if (teacherMapper.insert(teacherPo) == 1) {
			return teacherPo.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public List<TeacherDto> listAll() {
		List<TeacherPo> teacherList = teacherMapper.selectAll();
		List<TeacherDto> teacherDtoList = new ArrayList<TeacherDto>();
		for (TeacherPo teacherPo : teacherList) {
			TeacherDto teacherDto = new TeacherDto();
			BeanUtil.copyPropertiesSelective(teacherPo, teacherDto);
			teacherDtoList.add(teacherDto);
		}
		return teacherDtoList;
	}

	@Override
	public TeacherDto findById(Integer teacherId) {
		TeacherDto teacherDto = new TeacherDto();
		TeacherPo teacherPo = teacherMapper.selectByPrimaryKey(teacherId);
		BeanUtil.copyProperties(teacherPo, teacherDto);
		return teacherDto;
	}

	@Override
	public Integer modify(TeacherDto teacherDto) {
		TeacherPo teacherPo = new TeacherPo();
		BeanUtil.copyProperties(teacherDto, teacherPo);
		if (teacherMapper.updateByPrimaryKey(teacherPo) == 1) {
			return teacherPo.getTeacherId();
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
