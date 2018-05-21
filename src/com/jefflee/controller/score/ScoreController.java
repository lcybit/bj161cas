package com.jefflee.controller.score;

import com.jefflee.entity.score.Score;
import com.jefflee.entity.shiro.TbAdmin;
import com.jefflee.service.score.ScoreService;
import com.jefflee.util.ExcelUtil;
import com.jefflee.util.shiro.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGL on 2018/5/15.
 */
@RestController
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

    @RequestMapping(value = "/listByExamId/{examId}", method = RequestMethod.GET)
    public List<Score> listByExamId(@PathVariable("examId") Integer examId) {
        List<Score> scoreList = scoreService.selectList(examId);
        return scoreList;
    }

    @RequestMapping(value = "/import/{info}", method = RequestMethod.POST)
    public ResultUtil importExcel(@RequestParam MultipartFile file, @PathVariable("info") String info) {
        ResultUtil result = new ResultUtil();
        // 判断文件是否为空
        if (file.isEmpty()) {
            result.setCode(100);
            result.setMsg("文件为空");
            return result;
        }
        // 验证文件名是否合格
        if (!ExcelUtil.validateExcel(file.getOriginalFilename())) {
            result.setCode(100);
            result.setMsg("必须是Excel文件！");
            return result;
        }
        // 批量导入
        Integer outcome = null;
        try {
            outcome = scoreService.importExcel(file, info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (outcome == null) {
            result.setCode(100);
            result.setMsg("导入失败！");
            return result;
        } else {
            result.setCode(200);
            result.setMsg("导入成功！");
            return result;
        }
    }

    @RequestMapping(value = "/queryAuth")
    public String queryAuth() {
        Subject subject= SecurityUtils.getSubject();
        TbAdmin admin = (TbAdmin) subject.getPrincipal();
        return admin.getRoleId().toString();
    }
}
