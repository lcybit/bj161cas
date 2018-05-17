package com.jefflee.service.score.impl;

import com.jefflee.entity.score.FatherExam;
import com.jefflee.mapper.score.FatherExamMapper;
import com.jefflee.service.score.FatherExamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by TGL on 2018/5/17.
 */
@Service("fatherExamService")
public class FatherExamServiceImpl implements FatherExamService {
    
    @Resource(name = "fatherExamMapper")
    FatherExamMapper examMapper;

    @Override
    public Integer insert(FatherExam exam) {
        if (examMapper.insert(exam) == 1)
            return exam.getId();
        else return null;
    }

    @Override
    public List<FatherExam> selectList() {
        return examMapper.selectAll();
    }

    @Override
    public FatherExam selectById(Integer id) {
        return examMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateById(FatherExam exam) {
        if (examMapper.updateByPrimaryKey(exam) == 1)
            return exam.getId();
        else  return null;
    }

    @Override
    public Integer deleteById(Integer id) {
        if (examMapper.deleteByPrimaryKey(id) == 1)
            return id;
        else return null;
    }
}
