package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.config.Result;
import com.example.demo.entity.log;
import com.example.demo.entity.student;
import com.example.demo.mapper.LogMapper;
import com.example.demo.mapper.StudentMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController //声明该controller返回json
@RequestMapping("/student") //定义该接口统一路由
public class StudentController {

    //    将mapper引入controller
    @Resource
    StudentMapper studentMapper;
    @Resource
    LogMapper LogMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody student student) {
        System.out.println("进入后端登录");
        QueryWrapper<student> queryWrapper = new QueryWrapper<>();

        if(student.getSid()==null){
            return Result.error("-21", "请输入学号！");
        }
        if(student.getPassword()==null){
            return Result.error("-23", "请输入密码！");
        }

        queryWrapper.eq("sid", student.getSid());
        queryWrapper.eq("password", student.getPassword());

        student res = studentMapper.selectOne(queryWrapper);
        if (res == null) {
            return Result.error("-1", "学号或密码错误");
        }
        else{
            System.out.println("记录登录日志");
            log log=new log();
            log.setId(student.getSid());
            log.setType("student");
            log.setLogintime(new Timestamp(new Date().getTime()));
            LogMapper.insert(log);
            return Result.success(res);
        }
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody student student) {
        if(student.getSid()==null){
            return Result.error("-21", "请输入学号！");
        }
        if(student.getPassword()==null){
            return Result.error("-23", "请输入密码！");
        }
        if(student.getSname()==null) {
            return Result.error("-24", "请输入姓名！");
        }
        if(student.getIdnum()==null){
            return Result.error("-26", "请输入身份证号！");
        }


        student res = studentMapper.selectOne(Wrappers.<com.example.demo.entity.student>lambdaQuery().eq(com.example.demo.entity.student::getSid, student.getSid()));
        if (res != null) {
            return Result.error("-2", "学号已注册");
        }
        if (student.getPassword() == null) {
            student.setPassword("123456");
        }
        studentMapper.insert(student);
        return Result.success();
    }

    @PostMapping("/register/{confirm}")
    public Result<?> register2(@RequestBody student student,@PathVariable String confirm) {
        if(student.getSid()==null){
            return Result.error("-21", "请输入学号！");
        }
        if(student.getPassword()==null){
            return Result.error("-23", "请输入密码！");
        }
        if(student.getSname()==null){
            return Result.error("-24", "请输入姓名！");
        }
        if(confirm==null){
            return Result.error("-25", "请输入确认密码！");
        }
        if(student.getIdnum()==null){
            return Result.error("-26", "请输入身份证号！");
        }
        if(student.getPassword()!=confirm){
            return Result.error("-27", "请确认两次输入密码一致！");
        }

        student res = studentMapper.selectOne(Wrappers.<com.example.demo.entity.student>lambdaQuery().eq(com.example.demo.entity.student::getSid, student.getSid()));
        if (res != null) {
            return Result.error("-2", "学号已注册");
        }
        if (student.getPassword() == null) {
            student.setPassword("123456");
        }
        studentMapper.insert(student);
        return Result.success();
    }

    @PostMapping
//    实现学生表的新增功能
    public Result<?> save(@RequestBody student student) {
        studentMapper.insert(student);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody student student) {
        studentMapper.updateById(student);
        return Result.success();
    }

    @DeleteMapping("/{sid}")
    public Result<?> delete(@PathVariable String sid) {
        studentMapper.deleteById(sid);
        return Result.success();
    }

    //学生修改个人信息前查询预加载
    @GetMapping("/{sid}")
    public Result<?> select(@PathVariable String sid) {
        return Result.success(studentMapper.selectById(sid));
    }

    //找回密码
    @GetMapping("/findPassword/{sid}")
    public Result<?> findPassword(@PathVariable String sid) {
        System.out.println("尝试找回密码");

        if(studentMapper.selectById(sid)!=null) {
            student student= studentMapper.selectById(sid);
//            System.out.println(student);
            return Result.success(student);
        }
        else{
            return Result.error("-4","未查询到该学号，请检查输入");
        }
    }

    //重置密码
    @PutMapping("/reset/{sid}")
    public Result<?> resetPassword(@PathVariable String sid) {
        System.out.println("尝试重置密码");

        if(studentMapper.selectById(sid)!=null) {
            student thisstudent=studentMapper.selectById(sid);
            thisstudent.setPassword("123456");
            studentMapper.updateById(thisstudent);
            return Result.success(thisstudent);
        }
        else{
            return Result.error("-4","未查询到该学号，请检查输入");
        }
    }


}
