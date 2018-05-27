package com.jefflee.service.information;

import com.jefflee.entity.information.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by TGL on 2018/5/15.
 */
public interface StudentService {

    Integer insert(Student student);

    List<Student> selectList();

    Student selectById(Integer id) throws Exception;

    Integer updateById(Student student);

    Integer deleteById(Integer id);

    List<Student> selectListByGradeId(Integer gradeId);

    List<Student> selectListByTClassId(Integer tClassId);

    Map<String, Object> importExcel(MultipartFile file) throws Exception;
}
