package com.jefflee.mapper.information;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Course;
import com.jefflee.util.MyMapper;

@Repository("courseMapper")
public interface CourseMapper extends MyMapper<Course> {

	@Select("select * from info_course c, rlat_grade_course r where c.course_id = r.course_id and r.grade_id = #{gradeId}")
	@Results({ @Result(id = true, column = "course_id", property = "courseId") })
	public List<Course> selectListByGradeId(Integer gradeId);

}
