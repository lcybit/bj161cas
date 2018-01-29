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
import com.jefflee.util.StringUtil;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

	@Resource(name = "teacherService")
	TeacherService teacherService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody TeacherDto teacherDto) {
		Map<String, String> response = new HashMap<String, String>();
		String teacherId = teacherService.create(teacherDto);
		response.put("teacherId", teacherId);
		if (StringUtil.isBlank(teacherId)) {
			response.put("msg", "fail");
		} else {
			response.put("msg", "success");
		}
		return response;
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
	public Map<String, String> modify(@RequestBody TeacherDto teacherDto) {
		Map<String, String> response = new HashMap<String, String>();
		String teacherId = teacherService.modify(teacherDto);
		response.put("teacherId", teacherId);
		if (StringUtil.isBlank(teacherId)) {
			response.put("msg", "fail");
		} else {
			response.put("msg", "success");
		}
		return response;
	}

	@RequestMapping(value = "/delete/{teacherId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("teacherId") String teacherId) {
		Map<String, String> response = new HashMap<String, String>();
		response.put("teacherId", teacherService.delete(teacherId));
		if (StringUtil.isBlank(teacherId)) {
			response.put("msg", "fail");
		} else {
			response.put("msg", "success");
		}
		return response;
	}
}
