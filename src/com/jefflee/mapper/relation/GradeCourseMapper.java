package com.jefflee.mapper.relation;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.relation.GradeCourse;
import com.jefflee.util.MyMapper;

@Repository("gradeCourseMapper")
public interface GradeCourseMapper extends MyMapper<GradeCourse> {

}