package com.jefflee.service.score.impl;

import com.jefflee.entity.score.Exam;
import com.jefflee.mapper.score.ExamMapper;
import com.jefflee.service.score.ExamService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by TGL on 2018/5/16.
 */
@Service("examService")
public class ExamServiceImpl implements ExamService{

    @Resource(name = "examMapper")
    ExamMapper examMapper;

    @Override
    public Integer insert(Exam exam) {
       if (examMapper.insert(exam) == 1)
           return exam.getId();
       else return null;
    }

    @Override
    public List<Exam> selectList(Integer examId) {
        Example example = new Example(Exam.class);
        example.createCriteria().andEqualTo("exam_no", examId);
        return examMapper.selectByExample(example);
    }

    @Override
    public Exam selectById(Integer id) {
        return examMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateById(Exam exam) {
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
