package com.jefflee.service.score;

import com.jefflee.entity.score.Score;
import com.jefflee.util.shiro.ResultUtil;

import java.util.List;

/**
 * Created by TGL on 2018/5/15.
 */
public interface ScoreService {

    public ResultUtil insert(Score score);

    public List<Score> queryList();

    public Score load(Integer id);

    public ResultUtil update(Integer id);

    public ResultUtil delete(Integer id);
}
