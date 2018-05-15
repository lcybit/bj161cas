package com.jefflee.controller.score;

import com.jefflee.util.shiro.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by TGL on 2018/5/15.
 */
@Controller
@RequestMapping("/score")
public class ScoreController {

    @RequestMapping("/insert")
    public ResultUtil insert(){
        return null;
    }
}
