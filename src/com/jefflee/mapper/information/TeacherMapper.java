package com.jefflee.mapper.information;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Teacher;
import com.jefflee.util.MyMapper;

@Repository("teacherMapper")
public interface TeacherMapper extends MyMapper<Teacher> {

	@Select("SELECT * FROM info_teacher t, schd_grade g, rlat_grade_teacher r "
			+ "WHERE g.grade_id = r.grade_id AND t.teacher_id = r.teacher_id AND g.grade_id = #{gradeId} ORDER BY convert(t.name using gbk)")
	@Results({ @Result(id = true, column = "teacher_id", property = "teacherId") })
	List<Teacher> selectListByGradeId(Integer gradeId);

	@Select("SELECT * FROM info_teacher t ORDER BY convert(t.name using gbk)")
	@Results({ @Result(id = true, column = "teacher_id", property = "teacherId") })
	List<Teacher> selectList();
}
