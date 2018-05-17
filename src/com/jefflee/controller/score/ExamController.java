package com.jefflee.controller.score;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.score.Exam;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.score.ExamService;
import com.jefflee.util.shiro.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TGL on 2018/5/16.
 */
@RestController
@RequestMapping(value = "/exam")
public class ExamController {

    @Resource(name = "examService")
    ExamService examService;

    @Resource(name = "courseService")
    CourseService courseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultUtil create(@RequestBody Exam exam){
        ResultUtil result = new ResultUtil();
        List<Course> courseList = courseService.selectList();
        int count = 0;
        for (Course course : courseList){
            if (course.getName().equals(exam.getCourse_name())){
                exam.setCourse_id(course.getCourseId());
                break;
            }
            count++;
        }
        if (count == courseList.size()){
            result.setCode(100);
            result.setMsg("请输入正确的课程名称");
            return result;
        }

        Integer examId = examService.insert(exam);
        if (examId != null) {
            result.setCode(200);
            result.setMsg("新增成功");
        }
        return result;
    }

    @RequestMapping(value = "/list/{examId}", method = RequestMethod.GET)
    public List<Exam> listAll(@PathVariable("examId") Integer examId) {
        List<Course> courseList = courseService.selectList();
        Map<Integer, String> map = new HashMap<>();
        List<Exam> examList = examService.selectList(examId);
        for (Course course : courseList){
          map.put(course.getCourseId(), course.getName());
        }

        for (Exam exam : examList){
            exam.setCourse_name(map.get(exam.getCourse_id()));
        }
        return examList;
    }

//    @RequestMapping(value = "/list/{gradeId}", method = RequestMethod.GET)
//    public List<Exam> listByGradeId(@PathVariable("gradeId") Integer gradeId) {
//        return examService.selectListByGradeId(gradeId);
//    }

    @RequestMapping(value = "/find/{examId}", method = RequestMethod.GET)
    public Exam findById(@PathVariable("examId") Integer examId) {
        Map<Integer, String> map = new HashMap<>();
        List<Course> courseList = courseService.selectList();
        for (Course course : courseList){
            map.put(course.getCourseId(), course.getName());
        }
        Exam exam = examService.selectById(examId);
        exam.setCourse_name(map.get(exam.getCourse_id()));
        return exam;
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResultUtil modify(@RequestBody Exam exam) {
        ResultUtil result = new ResultUtil();
        List<Course> courseList = courseService.selectList();
        int count = 0;
        for (Course course : courseList){
            if (course.getName().equals(exam.getCourse_name())){
                exam.setCourse_id(course.getCourseId());
                break;
            }
            count++;
        }
        if (count == courseList.size()){
            result.setCode(100);
            result.setMsg("请输入正确的课程名称");
            return result;
        }

        Integer examId = examService.updateById(exam);
        if (examId != null) {
            result.setCode(200);
            result.setMsg("修改成功");
        }
        return result;
    }

    @RequestMapping(value = "/delete/{examId}", method = RequestMethod.DELETE)
    public ResultUtil delete(@PathVariable("examId") Integer examId) {
        ResultUtil result = new ResultUtil(200, "删除成功");
        examId = examService.deleteById(examId);
        if (examId == null) {
            result.setMsg("删除失败");
            result.setCode(100);
        }
        return result;
    }

}
