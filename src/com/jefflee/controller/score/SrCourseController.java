package com.jefflee.controller.score;

import com.jefflee.entity.score.SrCourse;
import com.jefflee.service.score.SrCourseService;
import com.jefflee.util.shiro.ResultUtil;
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
    public ResultUtil create(@RequestBody SrCourse course) {
        ResultUtil result = new ResultUtil(200, "新增成功");
        Integer courseId = courseService.insert(course);
        if (courseId == null) {
            result.setCode(100);
            result.setMsg("新增失败");
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
    public ResultUtil modify(@RequestBody SrCourse course) {
        ResultUtil result = new ResultUtil(200, "修改成功");
        Integer courseId = courseService.updateById(course);
        if (courseId == null) {
            result.setCode(100);
            result.setMsg("修改失败");
        }
        return result;
    }

    @RequestMapping(value = "/delete/{courseId}", method = RequestMethod.DELETE)
    public ResultUtil delete(@PathVariable("courseId") Integer courseId) {
        ResultUtil result = new ResultUtil(200, "删除成功");
        courseId = courseService.deleteById(courseId);
        if (courseId == null) {
            result.setMsg("删除失败");
            result.setCode(100);
        }
        return result;
    }
}
