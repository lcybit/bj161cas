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

import com.jefflee.entity.relation.GradeCourse;
import com.jefflee.service.relation.GradeCourseService;

@RestController
@RequestMapping(value = "/grade_course")
public class GradeCourseController {

	@Resource(name = "gradeCourseService")
	GradeCourseService gradeCourseService;

	@RequestMapping(value = "/find/{gradeId}", method = RequestMethod.GET)
	public List<GradeCourse> findById(@PathVariable("gradeId") Integer gradeId) {
		return gradeCourseService.selectByGradeId(gradeId);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody GradeCourse gradeCourse) {
		Map<String, String> result = new HashMap<String, String>();
		Integer gradeCourseId = gradeCourseService.insert(gradeCourse);
		if (gradeCourseId != null) {
			result.put("gradeCourseId", gradeCourseId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public Map<String, String> remove(@RequestBody GradeCourse gradeCourse) {
		Map<String, String> result = new HashMap<String, String>();
		gradeCourseService.delete(gradeCourse);
		result.put("success", "true");
		return result;
	}
}
