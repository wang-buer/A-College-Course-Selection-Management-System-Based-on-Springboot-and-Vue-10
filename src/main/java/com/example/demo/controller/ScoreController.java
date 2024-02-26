package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.Result;
import com.example.demo.entity.score;
import com.example.demo.entity.tcannual;
import com.example.demo.mapper.ScoreMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

@RestController //声明该controller返回json
@RequestMapping("/score") //定义该接口统一路由
public class ScoreController {

    //    将mapper引入controller
    @Resource
    ScoreMapper scoreMapper;
    
    @PostMapping
//    成绩新增功能
    public Result<?> save(@RequestBody score score) {
        scoreMapper.insert(score);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody score score) {
//        Wrapper<tcannual> Wrapper=Wrappers.<tcannual>lambdaQuery().eq(tcannual::getTid,tc.getTid()).eq(tcannual::getCid,tc.getCid());
//        System.out.println(tc);
//        TCannualMapper.update(tc,Wrapper);
//        scoreMapper.updateById(score);
        return Result.success();
    }

    @DeleteMapping("/{sid}")
    public Result<?> delete(@PathVariable String sid) {
//        scoreMapper.deleteById(sid);
        System.out.println("删除成绩");
        return Result.success();
    }

    //查询预加载学生个人成绩
    @GetMapping("/{sid}")
    public Result<?> select(@PathVariable String sid) {
        return Result.success(scoreMapper.selectById(sid));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String select,
                              @RequestParam(defaultValue = "") String sid) {
        LambdaQueryWrapper<score> wrapper = Wrappers.<score>lambdaQuery();
        wrapper.eq(score::getSid, sid);

        //只显示已公布的成绩
        wrapper.eq(score::getIsupload, true);
        if (StrUtil.isNotBlank(search)) {
            if(select.equals("cid")) {
                wrapper.eq(score::getCid, search);
            }
            if(select.equals("cname")) {
                wrapper.like(score::getCname, search);
            }
            if(select.equals("term")) {
                wrapper.like(score::getTerm, search);
            }
        }




        Page<score> scorePage = scoreMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(scorePage);
    }


}
