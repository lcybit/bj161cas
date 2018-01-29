package com.jefflee.controller.information;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.dto.information.TeacherDto;
import com.jefflee.service.information.teacher.TeacherService;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

	@Resource(name = "teacherService")
	TeacherService teacherService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@RequestBody TeacherDto teacherDto) {
		return teacherService.create(teacherDto);
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public List<TeacherDto> findAll() {
		return teacherService.findAll();
	}

	@RequestMapping(value = "/find/{teacherId}", method = RequestMethod.GET)
	public TeacherDto findById(@PathVariable("teacherId") String teacherId) {
		return teacherService.findById(teacherId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@RequestBody TeacherDto teacherDto) {
		return teacherService.modify(teacherDto);
	}

	@RequestMapping(value = "/delete/{teacherId}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("teacherId") String teacherId) {
		return teacherService.delete(teacherId);
	}
}
