package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.config.Result;
import com.example.demo.entity.log;
import com.example.demo.entity.teacher;
import com.example.demo.mapper.LogMapper;
import com.example.demo.mapper.TeacherMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

@RestController //声明该controller返回json
@RequestMapping("/teacher") //定义该接口统一路由
public class TeacherController {

    //    将mapper引入controller
    @Resource
    TeacherMapper teacherMapper;
    @Resource
    LogMapper LogMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody teacher teacher) {
        QueryWrapper<teacher> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("tid", teacher.getTid());
        queryWrapper.eq("password", teacher.getPassword());

        teacher res = teacherMapper.selectOne(queryWrapper);
        if (res == null) {
            return Result.error("-1", "教职工号或密码错误");
        }
        else{
            System.out.println("记录教师登录日志");
            log log=new log();
            log.setId(teacher.getTid());
            log.setType("teacher");
            log.setLogintime(new Timestamp(new Date().getTime()));
            LogMapper.insert(log);
            return Result.success(res);
        }
    }

    @PostMapping
//    实现教师表的新增功能
    public Result<?> save(@RequestBody teacher teacher) {
        teacherMapper.insert(teacher);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody teacher teacher) {
        teacherMapper.updateById(teacher);
        return Result.success();
    }

    @DeleteMapping("/{tid}")
    public Result<?> delete(@PathVariable String tid) {
        teacherMapper.deleteById(tid);
        return Result.success();
    }

    //教师修改个人信息前查询预加载
    @GetMapping("/{tid}")
    public Result<?> select(@PathVariable String tid) {
        return Result.success(teacherMapper.selectById(tid));
    }

    //找回密码
    @GetMapping("/findPassword/{tid}")
    public Result<?> findPassword(@PathVariable String tid) {
        System.out.println("尝试找回密码");

        if(teacherMapper.selectById(tid)!=null) {
            teacher teacher= teacherMapper.selectById(tid);
//            System.out.println(teacher);
            return Result.success(teacher);
        }
        else{
            return Result.error("-4","未查询到该学号，请检查输入");
        }
    }

    //重置密码
    @PutMapping("/reset/{tid}")
    public Result<?> resetPassword(@PathVariable String tid) {
        System.out.println("尝试重置密码");

        if(teacherMapper.selectById(tid)!=null) {
            teacher thisteacher=teacherMapper.selectById(tid);
            thisteacher.setPassword("123456");
            teacherMapper.updateById(thisteacher);
            return Result.success(thisteacher);
        }
        else{
            return Result.error("-4","未查询到该学号，请检查输入");
        }
    }


}
