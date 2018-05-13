package com.jefflee.controller.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.information.Course;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.relation.GradeCourseService;

@RestController
@RequestMapping(value = "/course")
public class CourseController {

	@Resource(name = "courseService")
	CourseService courseService;

	@Resource(name = "gradeCourseService")
	GradeCourseService gradeCourseService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Course course) {
		Map<String, String> result = new HashMap<String, String>();
		Integer courseId = courseService.insert(course);
		if (courseId != null) {
			result.put("courseId", courseId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Course> listAll() {
		List<Course> courseList = new ArrayList<Course>();
		courseList = courseService.selectList();
		return courseList;
	}

	@RequestMapping(value = "/find/{courseId}", method = RequestMethod.GET)
	public Course findById(@PathVariable("courseId") Integer courseId) {
		return courseService.selectById(courseId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Course course) {
		Map<String, String> result = new HashMap<String, String>();
		Integer courseId = courseService.updateById(course);
		if (courseId != null) {
			result.put("courseId", courseId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{courseId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("courseId") Integer courseId) {
		Map<String, String> result = new HashMap<String, String>();
		courseId = courseService.deleteById(courseId);
		if (courseId != null) {
			result.put("courseId", courseId.toString());
		}
		return result;
	}
}
