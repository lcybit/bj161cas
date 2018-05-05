package com.jefflee.controller.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.relation.GradeTeacher;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.relation.GradeTeacherService;

@RestController
@RequestMapping(value = "/relat")
public class GradeTeacherController {
	@Resource(name = "gradeTeacherService")
	GradeTeacherService gradeTeacherService;
	@Resource(name = "teacherService")
	TeacherService teacherService;

	@RequestMapping(value = "/choose", method = RequestMethod.POST)
	public Map<String, String> choose(@RequestParam("gradeId") Integer gradeid,
			@RequestParam("IdArray") List<Integer> array) {
		Map<String, String> result = new HashMap<String, String>();
		Integer j = null, flag = 0;
		gradeTeacherService.deleteByGradeId(gradeid);// 删除数据库rlat_grade_teacher表原有选课数据

		for (int i = 0; i < array.size(); i++) {
			GradeTeacher gradeTeacher = new GradeTeacher();
			gradeTeacher.setTeacherId(array.get(i));
			gradeTeacher.setGradeId(gradeid);
			j = gradeTeacherService.insert(gradeTeacher);
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
	public List<GradeTeacher> findById(@PathVariable("gradeId") Integer gradeId) {
		return gradeTeacherService.selectByGradeId(gradeId);
	}

	@RequestMapping(value = "/check/{gradeId}", method = RequestMethod.GET)
	public List<Teacher> checkById(@PathVariable("gradeId") Integer gradeId) {
		List<GradeTeacher> gradeTeacherList = new ArrayList<GradeTeacher>();
		List<Teacher> teacherList = new ArrayList<Teacher>();
		gradeTeacherList = gradeTeacherService.selectByGradeId(gradeId);
		// 利用gradeTeacherList 里的每一个gradeTeacher.getTeacherId()得到
		// 每一个teacher，再转化成teacher，放入teacherList中
		for (GradeTeacher gradeTeacher : gradeTeacherList) {
			Teacher teacher = new Teacher();
			teacher = teacherService.selectById(gradeTeacher.getTeacherId());
			// 将 teacher转化 成 teacher
			teacherList.add(teacher);
		}
		return teacherList;
	}

}
