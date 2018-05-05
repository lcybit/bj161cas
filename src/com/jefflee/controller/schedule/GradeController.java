package com.jefflee.controller.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.schedule.Grade;
import com.jefflee.service.schedule.GradeService;

@RestController
@RequestMapping(value = "/grade")
public class GradeController {

	@Resource(name = "gradeService")
	GradeService gradeService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Grade grade) {
		Map<String, String> result = new HashMap<String, String>();
		Integer gradeId = gradeService.insert(grade);
		if (gradeId != null) {
			result.put("gradeId", gradeId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Grade> listAll() {
		return gradeService.selectList();
	}

	@RequestMapping(value = "/find/{gradeId}", method = RequestMethod.GET)
	public Grade findById(@PathVariable("gradeId") Integer gradeId) {
		return gradeService.selectById(gradeId);
	}

	// @RequestBody 将请求的信息的json串转成Grade对象
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Grade grade) {
		Map<String, String> result = new HashMap<String, String>();
		Integer gradeId = gradeService.updateById(grade);
		if (gradeId != null) {
			result.put("gradeId", gradeId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{gradeId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("gradeId") Integer gradeId) {
		Map<String, String> result = new HashMap<String, String>();
		gradeId = gradeService.deleteById(gradeId);
		if (gradeId != null) {
			result.put("gradeId", gradeId.toString());
		}
		return result;
	}
}
