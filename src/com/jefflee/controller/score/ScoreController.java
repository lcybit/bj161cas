package com.jefflee.controller.score;

import com.jefflee.entity.score.Score;
import com.jefflee.service.score.ScoreService;
import com.jefflee.util.shiro.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by TGL on 2018/5/15.
 */
@Controller
@RequestMapping("/score")
public class ScoreController {

    @Resource(name = "scoreService")
    ScoreService scoreService;

    @RequestMapping("/create")
    public ResultUtil create(@RequestBody Score score){
        ResultUtil result = new ResultUtil(200, "新增成功");
        if (scoreService.insert(score) == null) {
            result.setCode(100);
            result.setMsg("新增失败");
        }
        return result;
    }
}
