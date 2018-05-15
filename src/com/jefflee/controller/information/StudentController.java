package com.jefflee.controller.information;

import com.jefflee.entity.information.Student;
import com.jefflee.service.information.StudentService;
import com.jefflee.util.ExcelUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TGL on 2018/5/15.
 */
@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Resource(name = "studentService")
    StudentService studentService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, String> create(@RequestBody Student student) {
        Map<String, String> result = new HashMap<>();
        Integer studentId = studentService.insert(student);
        if (studentId != null) {
            result.put("studentId", studentId.toString());
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Student> listAll() {
        return studentService.selectList();
    }

    @RequestMapping(value = "/list/{gradeId}", method = RequestMethod.GET)
    public List<Student> listByGradeId(@PathVariable("gradeId") Integer gradeId) {
        return studentService.selectListByGradeId(gradeId);
    }

    @RequestMapping(value = "/find/{studentId}", method = RequestMethod.GET)
    public Student findById(@PathVariable("studentId") Integer studentId) {
        return studentService.selectById(studentId);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, String> modify(@RequestBody Student student) {
        Map<String, String> result = new HashMap<>();
        Integer studentId = studentService.updateById(student);
        if (studentId != null) {
            result.put("studentId", studentId.toString());
        }
        return result;
    }

    @RequestMapping(value = "/delete/{studentId}", method = RequestMethod.DELETE)
    public Map<String, String> delete(@PathVariable("studentId") Integer studentId) {
        Map<String, String> result = new HashMap<>();
        studentId = studentService.deleteById(studentId);
        if (studentId != null) {
            result.put("studentId", studentId.toString());
        }
        return result;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Map<String, String> importExcel(@RequestParam MultipartFile file) throws Exception {

        Map<String, String> result = new HashMap<String, String>();
        // 判断文件是否为空
        if (file.isEmpty()) {
            result.put("result", "文件为空");
            return result;
        }
        // 验证文件名是否合格
        if (!ExcelUtil.validateExcel(file.getOriginalFilename())) {
            result.put("result", "必须是Excel文件！");
            return result;
        }
        // 批量导入
        Map<String, Object> outcome = studentService.importExcel(file);
        if (outcome == null) {
            result.put("result", "导入失败！");
            return result;
        } else {
            result.put("result", "导入成功！");
            return result;
        }

    }
}
