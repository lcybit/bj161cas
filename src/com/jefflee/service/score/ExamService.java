package com.jefflee.service.score;

import com.jefflee.entity.score.Exam;

import java.util.List;

/**
 * Created by TGL on 2018/5/16.
 */
public interface ExamService {
    Integer insert(Exam exam);

    List<Exam> selectList(Integer examId);

    Exam selectById(Integer id);

    Integer updateById(Exam exam);

    Integer deleteById(Integer id);
}
