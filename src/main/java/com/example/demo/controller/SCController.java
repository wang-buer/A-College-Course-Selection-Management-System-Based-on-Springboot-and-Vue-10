package com.example.demo.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.config.Result;
import com.example.demo.entity.courseinfo;
import com.example.demo.entity.scannual;
import com.example.demo.entity.student;
import com.example.demo.entity.student_course;
import com.example.demo.mapper.SCMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController //声明该controller返回json
@RequestMapping("/SC") //定义该接口统一路由
public class SCController {

    //    将mapper引入controller
    @Resource
    SCMapper SCMapper;
    
    @PostMapping("/choosecourse/{sid}/{sname}")
//    实现学生的选课功能
    public Result<?> coursechoosing(@PathVariable String sid,
                                    @PathVariable String sname,
                                    @RequestBody student_course sc) {
        System.out.println(sc);
        //如果已存在该记录则报错
        if(SCMapper.selectOne(Wrappers.<student_course>lambdaQuery().eq(student_course::getSid,sid).eq(student_course::getCid,sc.getCid())) != null){
            System.out.println(sc.getCname()+" 历史中已存在该学生选该课程记录！");
            return Result.error("-5","您已选报过该课程！");
        }
        //否则插入
        else {
            int year=new Date().getYear();
            year+=1900;
            int month=new Date().getMonth();
            System.out.println(year);
            System.out.println(month);
            int temp;
            if(month>=9){temp=1;}
            else{temp=2;}
            sc.setTerm(year+"-"+temp);

            sc.setSid(sid);
            sc.setSname(sname);

            SCMapper.insert(sc);
            return Result.success();
        }
    }

    @DeleteMapping("/cancelcourse/{sid}")
    //    实现学生的退课功能
    public Result<?> delete(@PathVariable String sid,
                            @RequestBody student_course sc) {
        //如果不存在该记录则报错
        System.out.println(sc);
        Wrapper<student_course> Wrapper=Wrappers.<student_course>lambdaQuery().eq(student_course::getSid,sid).eq(student_course::getCid,sc.getCid());
        if(SCMapper.selectOne(Wrapper) == null){
            System.out.println(sc.getCname()+"历史中不存在该学生选该课程记录！");
            return Result.error("-7","您未选报过该课程！");
        }
        //否则删除
        else {
            SCMapper.delete(Wrapper);
            return Result.success();
        }

    }











}
