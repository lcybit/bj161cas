package com.jefflee.controller.relation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.relation.GradeCourse;
import com.jefflee.service.relation.GradeCourseService;

@RestController
@RequestMapping(value = "/relation")
public class GradeCourseController {

	@Resource(name = "gradeCourseService")
	GradeCourseService gradeCourseService;

	@RequestMapping(value = "/choose", method = RequestMethod.POST)
	public Map<String, String> choose(@RequestParam("gradeId") Integer gradeid,
			@RequestParam("IdArray") List<Integer> array) {
		Map<String, String> result = new HashMap<String, String>();
		Integer j = null, flag = 0;
		gradeCourseService.deleteByGradeId(gradeid);// 删除数据库rlat_grade_course表原有选课数据

		for (int i = 0; i < array.size(); i++) {
			GradeCourse gradeCourse = new GradeCourse();
			gradeCourse.setCourseId(array.get(i));
			gradeCourse.setGradeId(gradeid);
			j = gradeCourseService.insert(gradeCourse);
			if (j == null) {
				flag = 1;
				break;
			}
		}
		if (flag == 0) {
			result.put("gradeId", j.toString());
		} else {
			result.put("gradeId", j.toString());
		}
		return result;
	}

	@RequestMapping(value = "/find/{gradeId}", method = RequestMethod.GET)
	public List<GradeCourse> findById(@PathVariable("gradeId") Integer gradeId) {
		return gradeCourseService.selectByGradeId(gradeId);
	}
}
