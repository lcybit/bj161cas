package com.jefflee.controller.score;

import com.jefflee.entity.score.SrCourse;
import com.jefflee.service.score.SrCourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TGL on 2018/5/16.
 */
@RestController
@RequestMapping("/sr_course")
public class SrCourseController {

    @Resource(name = "srCourseService")
    SrCourseService courseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, String> create(@RequestBody SrCourse course) {
        Map<String, String> result = new HashMap<String, String>();
        Integer courseId = courseService.insert(course);
        if (courseId != null) {
            result.put("courseId", courseId.toString());
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<SrCourse> listAll() {
        List<SrCourse> courseList = new ArrayList<>();
        courseList = courseService.selectList();
        return courseList;
    }

    @RequestMapping(value = "/listByCourseId/{courseId}", method = RequestMethod.GET)
    public List<SrCourse> listByCourseId(@PathVariable("courseId") Integer courseId) {
        List<SrCourse> courseList = courseService.selectListByCourseId(courseId);
        return courseList;
    }

    @RequestMapping(value = "/find/{courseId}", method = RequestMethod.GET)
    public SrCourse findById(@PathVariable("courseId") Integer courseId) {
        return courseService.selectById(courseId);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, String> modify(@RequestBody SrCourse course) {
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
