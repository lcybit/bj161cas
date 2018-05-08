package com.jefflee.controller.relation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.relation.GradeTeacher;
import com.jefflee.service.relation.GradeTeacherService;

@RestController
@RequestMapping(value = "/grade_teacher")
public class GradeTeacherController {
	@Resource(name = "gradeTeacherService")
	GradeTeacherService gradeTeacherService;

	@RequestMapping(value = "/find/{gradeId}", method = RequestMethod.GET)
	public List<GradeTeacher> findByGradeId(@PathVariable("gradeId") Integer gradeId) {
		return gradeTeacherService.selectListByGradeId(gradeId);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody GradeTeacher gradeTeacher) {
		Map<String, String> result = new HashMap<String, String>();
		Integer gradeTeacherId = gradeTeacherService.insert(gradeTeacher);
		if (gradeTeacherId != null) {
			result.put("gradeTeacherId", gradeTeacherId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public Map<String, String> remove(@RequestBody GradeTeacher gradeTeacher) {
		Map<String, String> result = new HashMap<String, String>();
		gradeTeacherService.delete(gradeTeacher);
		result.put("success", "true");
		return result;
	}

}
