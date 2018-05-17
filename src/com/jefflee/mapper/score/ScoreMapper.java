package com.jefflee.mapper.score;

import com.jefflee.entity.score.Score;
import com.jefflee.util.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by TGL on 2018/5/17.
 */
@Repository("scoreMapper")
public interface ScoreMapper extends MyMapper<Score>{
}
