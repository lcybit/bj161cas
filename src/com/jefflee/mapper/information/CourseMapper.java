package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.po.information.CoursePo;

import tk.mybatis.mapper.common.Mapper;

@Repository("courseMapper")
public interface CourseMapper extends Mapper<CoursePo> {

}
