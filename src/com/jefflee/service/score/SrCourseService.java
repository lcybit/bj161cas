package com.jefflee.service.score;

import com.jefflee.entity.score.SrCourse;

import java.util.List;

/**
 * Created by TGL on 2018/5/16.
 */
public interface SrCourseService {
    Integer insert(SrCourse course);

    List<SrCourse> selectList();

    List<SrCourse> selectListByCourseId(Integer courseId);

    SrCourse selectById(Integer courseId);

    Integer updateById(SrCourse course);

    Integer deleteById(Integer courseId);
}
