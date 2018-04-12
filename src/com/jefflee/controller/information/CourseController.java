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

import com.jefflee.po.information.CoursePo;
import com.jefflee.po.relation.GroupCoursePo;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.relation.GroupCourseService;

@RestController
@RequestMapping(value = "c")
public class CourseController {

	@Resource(name = "courseService")
	CourseService courseService;

	@Resource(name = "groupCourseService")
	GroupCourseService groupCourseService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody CoursePo coursePo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer courseId = courseService.insert(coursePo);
		if (courseId != null) {
			result.put("courseId", courseId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<CoursePo> listAll() {
		return courseService.selectAll();
	}

	//TODO 可以delete
	@SuppressWarnings("null")
	@RequestMapping(value = "/check/{groupId}", method = RequestMethod.GET)
	public List<CoursePo> checkById(@PathVariable("groupId") Integer groupId) {
		List<GroupCoursePo> groupCoursePoList = groupCourseService.selectById(groupId);
		List<CoursePo> coursePoList = new ArrayList<CoursePo>();
		//根据groupid得到 groupcoursePolist 遍历groupcoursePolist，根据courseid 得到coursePoList
		for(int i=0;i<groupCoursePoList.size();i++)
		{
			Integer courseId = null;
			courseId = groupCoursePoList.get(i).getCourseId();
			coursePoList.add(courseService.selectById(courseId));
		}
		return coursePoList;
	}
	
	@RequestMapping(value = "/find/{courseId}", method = RequestMethod.GET)
	public CoursePo findById(@PathVariable("courseId") Integer courseId) {
		return courseService.selectById(courseId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody CoursePo coursePo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer courseId = courseService.updateById(coursePo);
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
