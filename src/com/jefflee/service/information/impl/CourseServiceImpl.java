package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import com.jefflee.entity.score.SrCourse;
import com.jefflee.mapper.score.SrCourseMapper;
import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.mapper.information.CourseMapper;
import com.jefflee.service.information.CourseService;
import tk.mybatis.mapper.entity.Example;

@Service("courseService")
public class CourseServiceImpl implements CourseService {

	@Resource(name = "courseMapper")
	private CourseMapper courseMapper;

	@Resource(name = "srCourseMapper")
	private SrCourseMapper srCourseMapper;

	@Override
	public Integer insert(Course course) {
		if (courseMapper.insert(course) == 1) {
			return course.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public List<Course> selectList() {
		return courseMapper.selectAll();
	}

	@Override
	public Course selectById(Integer courseId) {
		return courseMapper.selectByPrimaryKey(courseId);
	}

	@Override
	public List<Course> selectListByGradeId(Integer gradeId) {
		return courseMapper.selectListByGradeId(gradeId);
	}

	@Override
	public Integer updateById(Course course) {
		if (courseMapper.updateByPrimaryKey(course) == 1) {
			return course.getCourseId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer courseId) {
		if (courseMapper.deleteByPrimaryKey(courseId) == 1) {
			//删除父课程要把它下面的所有子课程删除
			Example example = new Example(SrCourse.class);
			example.createCriteria().andEqualTo("course_id", courseId);
			if (srCourseMapper.deleteByExample(example) == 1)
				return courseId;
			else return null;
		} else {
			return null;
		}
	}

}