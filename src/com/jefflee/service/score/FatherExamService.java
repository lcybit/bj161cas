package com.jefflee.service.score;

import com.jefflee.entity.score.FatherExam;

import java.util.List;

/**
 * Created by TGL on 2018/5/17.
 */
public interface FatherExamService {
    Integer insert(FatherExam exam);

    List<FatherExam> selectList();

    FatherExam selectById(Integer id);

    Integer updateById(FatherExam exam);

    Integer deleteById(Integer id);
}
