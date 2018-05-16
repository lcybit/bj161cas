package com.jefflee.service.score.impl;

import com.jefflee.entity.score.SrCourse;
import com.jefflee.mapper.score.SrCourseMapper;
import com.jefflee.service.score.SrCourseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by TGL on 2018/5/16.
 */
@Service("srCourseService")
public class SrCourseServiceImpl implements SrCourseService{
    
    @Resource(name = "srCourseMapper")
    SrCourseMapper courseMapper;
    
    @Override
    public Integer insert(SrCourse course) {
        if (courseMapper.insert(course) == 1) {
            return course.getId();
        } else {
            return null;
        }
    }

    @Override
    public List<SrCourse> selectList() {
        return courseMapper.selectAll();
    }

    @Override
    public List<SrCourse> selectListByCourseId(Integer courseId) {
        Example example = new Example(SrCourse.class);
        example.createCriteria().andEqualTo("course_id", courseId);
        return courseMapper.selectByExample(example);
    }

    @Override
    public SrCourse selectById(Integer courseId) {
        return courseMapper.selectByPrimaryKey(courseId);
    }

    @Override
    public Integer updateById(SrCourse course) {
        if (courseMapper.updateByPrimaryKey(course) == 1) {
            return course.getId();
        } else {
            return null;
        }
    }

    @Override
    public Integer deleteById(Integer courseId) {
        if (courseMapper.deleteByPrimaryKey(courseId) == 1) {
            return courseId;
        } else {
            return null;
        }
    }
}
