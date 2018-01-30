package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Course;

import tk.mybatis.mapper.common.Mapper;

@Repository("courseMapper")
public interface CourseMapper extends Mapper<Course> {

}
