package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Teacher;

import tk.mybatis.mapper.common.Mapper;

@Repository("teacherMapper")
public interface TeacherMapper extends Mapper<Teacher> {

}
