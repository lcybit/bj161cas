package com.jefflee.service.score.impl;


import com.jefflee.entity.score.Score;
import com.jefflee.mapper.score.ScoreMapper;
import com.jefflee.service.score.ScoreService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by TGL on 2018/5/15.
 */
@Service("scoreService")
public class ScoreServiceImpl implements ScoreService{

    @Resource(name = "scoreMapper")
    ScoreMapper scoreMapper;

    @Override
    public Integer insert(Score score) {
        if (scoreMapper.insert(score) == 1)
            return score.getId();
        else return null;
    }

    @Override
    public List<Score> selectList(Integer examId) {
        Example example = new Example(Score.class);
        example.createCriteria().andEqualTo("exam_id", examId);
        List<Score> scoreList = scoreMapper.selectByExample(example);
        return scoreList;
    }

    @Override
    public Score selectById(Integer id) {
        return scoreMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateById(Score score) {
        if (scoreMapper.updateByPrimaryKey(score) == 1)
            return score.getId();
        else return null;
    }

    @Override
    public Integer deleteById(Integer id) {
        if (scoreMapper.deleteByPrimaryKey(id) == 1)
            return id;
        else return null;
    }
}
