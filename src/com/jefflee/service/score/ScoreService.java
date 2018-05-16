package com.jefflee.service.score;

import com.jefflee.entity.score.Score;

import java.util.List;

/**
 * Created by TGL on 2018/5/15.
 */
public interface ScoreService {

    Integer insert(Score score);

    List<Score> selectList();

    Score selectById(Integer id);

    Integer updateById(Score score);

    Integer deleteById(Integer id);
}
