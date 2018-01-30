package com.jefflee.controller.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, String> create(@RequestBody TeacherDto teacherDto) {
		Map<String, String> result = new HashMap<String, String>();
		Integer teacherId = teacherService.create(teacherDto);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<TeacherDto> listAll() {
		return teacherService.listAll();
	}

	@RequestMapping(value = "/find/{teacherId}", method = RequestMethod.GET)
	public TeacherDto findById(@PathVariable("teacherId") Integer teacherId) {
		return teacherService.findById(teacherId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody TeacherDto teacherDto) {
		Map<String, String> result = new HashMap<String, String>();
		Integer id = teacherService.modify(teacherDto);
		if (id != null) {
			result.put("teacherId", id.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{teacherId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("teacherId") Integer teacherId) {
		Map<String, String> result = new HashMap<String, String>();
		teacherId = teacherService.delete(teacherId);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}
}
