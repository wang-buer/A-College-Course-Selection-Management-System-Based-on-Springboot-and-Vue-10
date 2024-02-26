package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.Result;
import com.example.demo.entity.log;
import com.example.demo.entity.log;
import com.example.demo.mapper.LogMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController //声明该controller返回json
@RequestMapping("/log") //定义该接口统一路由
public class LogController {

    //    将mapper引入controller
    @Resource
    LogMapper logMapper;
    
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "15") Integer pageSize) {
        LambdaQueryWrapper<log> wrapper = Wrappers.<log>lambdaQuery();
        Page<log> logPage = logMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(logPage);
    }

}
