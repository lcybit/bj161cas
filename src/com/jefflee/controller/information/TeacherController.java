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

import com.jefflee.entity.information.Teacher;
import com.jefflee.service.information.TeacherService;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

	@Resource(name = "teacherService")
	TeacherService teacherService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Teacher teacher) {
		Map<String, String> result = new HashMap<String, String>();
		Integer teacherId = teacherService.insert(teacher);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Teacher> listAll() {
		return teacherService.selectList();
	}

	@RequestMapping(value = "/find/{teacherId}", method = RequestMethod.GET)
	public Teacher findById(@PathVariable("teacherId") Integer teacherId) {
		return teacherService.selectById(teacherId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Teacher teacher) {
		Map<String, String> result = new HashMap<String, String>();
		Integer teacherId = teacherService.updateById(teacher);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{teacherId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("teacherId") Integer teacherId) {
		Map<String, String> result = new HashMap<String, String>();
		teacherId = teacherService.deleteById(teacherId);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}
}
