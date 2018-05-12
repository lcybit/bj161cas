package com.jefflee.service.information;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jefflee.entity.information.Teacher;

public interface TeacherService {

	Integer insert(Teacher teacher);

	List<Teacher> selectList();

	Teacher selectById(Integer teacherId);

	Integer updateById(Teacher teacher);

	Integer deleteById(Integer teacherId);

	List<Teacher> selectListByGradeId(Integer gradeId);

	Map<String, Object> importExcel(MultipartFile file) throws Exception;

}
