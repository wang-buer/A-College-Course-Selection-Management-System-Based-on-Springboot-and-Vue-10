package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.Result;
import com.example.demo.entity.course;
import com.example.demo.mapper.CourseMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@RestController //声明该controller返回json
@RequestMapping("/course") //定义该接口统一路由
public class CourseController {

    //    将mapper引入controller
    @Resource
    CourseMapper courseMapper;
    
    @PostMapping
//    新增课程功能
    public Result<?> save(@RequestBody course course) {
        //根据当前时间生成学期term属性
        String littleterm;
        if(new Date().getMonth()>=9||new Date().getMonth()<=2){littleterm="1";}//上半学期
        else{littleterm="2";}//下半学期
        String term=Calendar.getInstance().get(Calendar.YEAR)+"-"+littleterm;
        course.setTerm(term);

        //检验数据库里是否有这门课的数据
        course thiscourse=courseMapper.selectById(course.getCid());
        if(thiscourse==null||!thiscourse.getTerm().equals(term)){//如果数据库中没有这门课或学期数不同
            courseMapper.insert(course);}
        else{//否则更新
            courseMapper.updateById(course);
        }
        return Result.success();
    }

    @DeleteMapping("/{cid}")
    public Result<?> delete(@PathVariable String cid) {
        courseMapper.deleteById(cid);
        System.out.println("删除course");
        return Result.success();
    }

    //查询预加载学生个人成绩
    @GetMapping("/{sid}")
    public Result<?> select(@PathVariable String sid) {
        return Result.success(courseMapper.selectById(sid));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String select) {
        LambdaQueryWrapper<course> wrapper = Wrappers.<course>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if(select.equals("cid")) {
                wrapper.eq(course::getCid, search);
            }
            if(select.equals("cname")) {
                wrapper.like(course::getCname, search);
            }
            if(select.equals("term")) {
                wrapper.like(course::getTerm, search);
            }
        }
        Page<course> coursePage = courseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(coursePage);
    }


}
