package com.jefflee.controller.score;

import com.jefflee.entity.score.FatherExam;
import com.jefflee.service.score.FatherExamService;
import com.jefflee.util.shiro.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TGL on 2018/5/17.
 */
@RestController
@RequestMapping("/father_exam")
public class FatherExamController {
    @Resource(name = "fatherExamService")
    FatherExamService examService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultUtil create(@RequestBody FatherExam exam){
        ResultUtil result = new ResultUtil(200, "新增成功");
        Integer examId = examService.insert(exam);
        if (examId == null) {
            result.setCode(100);
            result.setMsg("新增失败");
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FatherExam> listAll() {
        List<FatherExam> examList = examService.selectList();
        return examList;
    }

//    @RequestMapping(value = "/list/{gradeId}", method = RequestMethod.GET)
//    public List<FatherExam> listByGradeId(@PathVariable("gradeId") Integer gradeId) {
//        return examService.selectListByGradeId(gradeId);
//    }

    @RequestMapping(value = "/find/{examId}", method = RequestMethod.GET)
    public FatherExam findById(@PathVariable("examId") Integer examId) {
        FatherExam exam = examService.selectById(examId);
        return exam;
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResultUtil modify(@RequestBody FatherExam exam) {
        ResultUtil result = new ResultUtil(200, "修改成功");
        Integer examId = examService.updateById(exam);
        if (examId == null) {
            result.setCode(100);
            result.setMsg("修改失败");
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
