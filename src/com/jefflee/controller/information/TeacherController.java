package com.jefflee.controller.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jefflee.entity.information.Teacher;
import com.jefflee.service.information.TeacherService;
import com.jefflee.util.ExcelUtil;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

	@Resource(name = "teacherService")
	TeacherService teacherService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Teacher teacher) {
		Map<String, String> result = new HashMap<String, String>();
		Integer teacherId = teacherService.insert(teacher);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Teacher> listAll() {
		return teacherService.selectList();
	}

	@RequestMapping(value = "/find/{teacherId}", method = RequestMethod.GET)
	public Teacher findById(@PathVariable("teacherId") Integer teacherId) {
		return teacherService.selectById(teacherId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Teacher teacher) {
		Map<String, String> result = new HashMap<String, String>();
		Integer teacherId = teacherService.updateById(teacher);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{teacherId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("teacherId") Integer teacherId) {
		Map<String, String> result = new HashMap<String, String>();
		teacherId = teacherService.deleteById(teacherId);
		if (teacherId != null) {
			result.put("teacherId", teacherId.toString());
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
		Map<String, Object> outcome = teacherService.importExcel(file);
		if (result == null) {

			result.put("result", "导入失败！");
			return result;
		} else {

			result.put("result", "导入成功！");
			return result;
		}

	}
}
