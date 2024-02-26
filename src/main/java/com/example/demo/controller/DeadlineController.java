package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.config.Result;
import com.example.demo.entity.deadline;
import com.example.demo.entity.tcannual;
import com.example.demo.mapper.DeadlineMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController //声明该controller返回json
@RequestMapping("/deadline") //定义该接口统一路由
public class DeadlineController {

    //    将mapper引入controller
    @Resource
    DeadlineMapper deadlineMapper;
    
    //找回密码
    @GetMapping
    public Result<?> getDeadline() {
        Wrapper<deadline> Wrapper= Wrappers.<deadline>lambdaQuery();
        deadline deadline= deadlineMapper.selectOne(Wrapper);
            return Result.success(deadline);
    }

    @PutMapping
//    实现老师的修改选课截止日期功能
    public Result<?> courseupdating(@RequestBody deadline ddl) {
        System.out.println(ddl);
        Wrapper<deadline> Wrapper= Wrappers.<deadline>lambdaQuery();

        deadlineMapper.update(ddl,Wrapper);
        return Result.success();
    }

}
