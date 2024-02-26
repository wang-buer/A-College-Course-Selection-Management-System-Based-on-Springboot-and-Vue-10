package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.Result;
import com.example.demo.entity.courseinfo;
import com.example.demo.entity.tcannual;
import com.example.demo.entity.teacherstudent;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.mapper.TCannualMapper;
import com.example.demo.mapper.TeacherStudentMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //声明该controller返回json
@RequestMapping("/teacherstudent2") //定义该接口统一路由
public class TeacherStudentController {

    //    将mapper引入controller
    @Resource
    TeacherStudentMapper tsMapper;

    //点名表
    @GetMapping("/{tid}/{cid}")
    public Result<?> teacherstudent(@PathVariable String tid,
                                    @PathVariable String cid) {
        LambdaQueryWrapper<teacherstudent> wrapper = Wrappers.<teacherstudent>lambdaQuery();
        wrapper.eq(teacherstudent::getTid, tid);
        wrapper.eq(teacherstudent::getCid, cid);
        return Result.success(tsMapper.selectList(wrapper));
    }

    @PostMapping
//    实现老师的点名功能
    public Result<?> studentcheckin(@RequestBody ArrayList<teacherstudent> teacherstudents) {

        for(teacherstudent ts:teacherstudents){
            LambdaQueryWrapper<teacherstudent> wrapper = Wrappers.<teacherstudent>lambdaQuery();
            wrapper.eq(teacherstudent::getSid, ts.getSid());
            wrapper.eq(teacherstudent::getCid, ts.getCid());
            tsMapper.update(ts,wrapper);
        }
        return Result.success();
    }












}
