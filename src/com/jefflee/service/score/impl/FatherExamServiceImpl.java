package com.jefflee.service.score.impl;

import com.jefflee.entity.score.Exam;
import com.jefflee.entity.score.FatherExam;
import com.jefflee.mapper.score.ExamMapper;
import com.jefflee.mapper.score.FatherExamMapper;
import com.jefflee.service.score.FatherExamService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by TGL on 2018/5/17.
 */
@Service("fatherExamService")
public class FatherExamServiceImpl implements FatherExamService {
    
    @Resource(name = "fatherExamMapper")
    FatherExamMapper fatherExamMapper;

    @Resource(name = "examMapper")
    ExamMapper examMapper;

    @Override
    public Integer insert(FatherExam exam) {
        if (fatherExamMapper.insert(exam) == 1)
            return exam.getId();
        else return null;
    }

    @Override
    public List<FatherExam> selectList() {
        return fatherExamMapper.selectAll();
    }

    @Override
    public FatherExam selectById(Integer id) {
        return fatherExamMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateById(FatherExam exam) {
        if (fatherExamMapper.updateByPrimaryKey(exam) == 1)
            return exam.getId();
        else  return null;
    }

    @Override
    public Integer deleteById(Integer id) {
        if (fatherExamMapper.deleteByPrimaryKey(id) == 1){
            //删除父级考试要把它下面的所有子考试删除
            Example example = new Example(Exam.class);
            example.createCriteria().andEqualTo("exam_no", id);
            if (examMapper.deleteByExample(example) == 1)
                return id;
            else return null;
        }
        else return null;
    }
}
