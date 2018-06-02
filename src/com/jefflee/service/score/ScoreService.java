package com.jefflee.service.score;

import com.jefflee.entity.score.Score;
import com.jefflee.util.shiro.ResultUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by TGL on 2018/5/15.
 */
public interface ScoreService {

    Integer insert(Score score);

    List<Score> selectList(Integer examId);

    Score selectById(Integer id);

    Integer updateById(Score score);

    Integer deleteById(Integer id);

    Integer importExcel(MultipartFile file, String info) throws Exception;

    ResultUtil selectScoreList(Integer examId, Integer studentId, Integer limit, Integer page);
}