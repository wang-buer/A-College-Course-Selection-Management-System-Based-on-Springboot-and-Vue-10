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
import com.example.demo.entity.scannual;
import com.example.demo.entity.teacherstudent;
import com.example.demo.mapper.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@RestController //声明该controller返回json
@RequestMapping("/TSC") //定义该接口统一路由
public class TSCController {

    //    将mapper引入controller
    @Resource
    SCannualMapper scannualMapper;
    @Resource
    ScoreMapper scoreMapper;
    @Resource
    TCannualMapper tcannualMapper;
    @Resource
    TSCMapper tscMapper;
    @Resource
    TeacherStudentMapper tsMapper;

    @PostMapping("/writescore")
//    实现成绩的新增功能
    public Result<?> writescore(@RequestBody score thisscore) {
//        System.out.println(thisscore);

        //补充设置属性
        //根据当前时间生成学期term属性
        String littleterm;
        if(new Date().getMonth()>=9||new Date().getMonth()<=2){littleterm="1";}//上半学期
        else{littleterm="2";}//下半学期
        String term=Calendar.getInstance().get(Calendar.YEAR)+"-"+littleterm;
        thisscore.setTerm(term);

        float gpa=thisscore.getCredit()*thisscore.getScore()/100;
        thisscore.setGpa(gpa);

        String state;
        if(thisscore.getScore()>=90){state="A";}
        else if(thisscore.getScore()>=80){state="B";}
        else if(thisscore.getScore()>=60){state="C";}
        else {state="D";}
        thisscore.setState(state);

        thisscore.setIsupload(true);//提交当前这条分数

        //如果这门课还没打过分
//        Map<String, Object> Map = new HashMap<String, Object>();
//        Map.put("tid", thisscore.getTid());
//        Map.put("cid", thisscore.getCid());
//        Map.put("sid", thisscore.getSid());
        Wrapper<score> Wrapper=Wrappers.<score>lambdaQuery().eq(score::getTid,thisscore.getTid()).eq(score::getSid,thisscore.getSid()).eq(score::getCid,thisscore.getCid());
//        scoreMapper.selectByMap(Map)
        if(scoreMapper.selectOne(Wrapper)==null){
            scoreMapper.insert(thisscore);
        }
        else{//否则更新
            scoreMapper.update(thisscore,Wrapper);
        }

        //在scannual补充该学生这门课的score
        Wrapper<scannual> Wrapper2=Wrappers.<scannual>lambdaQuery().eq(scannual::getSid,thisscore.getSid()).eq(scannual::getCid,thisscore.getCid());
        scannual thissc=scannualMapper.selectOne(Wrapper2);
        thissc.setScore(thisscore.getScore());
        scannualMapper.update(thissc,Wrapper2);

        return Result.success();
    }


    //获取打分名单 应该根据tid找tcannual的cid，再从scannual里找sid等
    @GetMapping("/loadscore/{tid}")
    public Result<?> loadscore(@PathVariable String tid) {
        List<score> readyscores = scoreMapper.loadscore(tid);
//        System.out.println(readyscores);//[score(sid=111111111111, sname=追逐着, cid=234234, cname=打分, term=null, score=0.0, credit=3.0, gpa=0.0, state=null)]

        return Result.success(readyscores);
        }

    //获取老师的学生点名册
    @GetMapping("/getsnames/{tid}")
    public Result<?> getsnames(@PathVariable String tid) {
        //查当前老师上的所有课的cid和cname
        List<String> cids=tscMapper.getcids(tid);
        List<String> cnames=tscMapper.getcnames(tid);
//        System.out.println(cids);
//        System.out.println(cnames);

        HashMap<String,List> result=new HashMap<>();

        //根据cid查上这门课的所有学生
        for(int i=0;i<cids.size();++i) {
            List<String> snames=tscMapper.getsnames(cids.get(i));
            //结果是课程名到学生名列表的HashMap

            if(snames.isEmpty()){
                ArrayList<String> nostudent=new ArrayList<>();
                nostudent.add("暂无学生");
                result.put(cnames.get(i),nostudent);
            }
            else{
                //如果点名表里没有该学生上这课的记录则添加 对老师上的每一个cid找sid
                List<String> sids=tscMapper.getsids(cids.get(i));
                for(int j=0;j<sids.size();++j) {
                    Wrapper<teacherstudent> Wrapper = Wrappers.<teacherstudent>lambdaQuery().eq(teacherstudent::getTid, tid).eq(teacherstudent::getSid, sids.get(j)).eq(teacherstudent::getCid, cids.get(i));
                    if (tsMapper.selectOne(Wrapper) == null) {
                        teacherstudent ts=new teacherstudent();
                        ts.setTid(tid);
                        ts.setSid(sids.get(j));
                        ts.setCid(cids.get(i));
                        ts.setSname(snames.get(j));
                        tsMapper.insert(ts);
                    }
                    //在结果集添加cname到snames的映射
                    result.put(cnames.get(i), snames);
                }
            }
        }

        return Result.success(result);
    }

    //暂存分数，学生将不会看到，并且不会生成排名
    @PostMapping("/tempsavescore")
    public Result<?> tempsavescore(@RequestBody ArrayList<score> scores) {
//        System.out.println(scores);

        //对传过来的scores数组中每一条score数据
        for (score thisscore:scores) {

            //补充设置属性
            //根据当前时间生成学期term属性
            String littleterm;
            if(new Date().getMonth()>=9||new Date().getMonth()<=2){littleterm="1";}//上半学期
            else{littleterm="2";}//下半学期
            String term=String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+littleterm;
            thisscore.setTerm(term);

            float gpa=thisscore.getCredit()*thisscore.getScore()/100;
            thisscore.setGpa(gpa);

            String state;
            if(thisscore.getScore()>=90){state="A";}
            else if(thisscore.getScore()>=80){state="B";}
            else if(thisscore.getScore()>=60){state="C";}
            else {state="D";}
            thisscore.setState(state);

            thisscore.setIsupload(false);//暂存当前这条分数

            System.out.println(thisscore);

            //如果这门课还没打过分，在score数据库中不存在
            Wrapper<score> Wrapper=Wrappers.<score>lambdaQuery().eq(score::getTid,thisscore.getTid()).eq(score::getSid,thisscore.getSid()).eq(score::getCid,thisscore.getCid());
            if(scoreMapper.selectOne(Wrapper)==null){
                scoreMapper.insert(thisscore);
            }
            else{//否则更新
                scoreMapper.update(thisscore,Wrapper);
            }

            //在scannual补充该学生这门课的score
            Wrapper<scannual> Wrapper2=Wrappers.<scannual>lambdaQuery().eq(scannual::getSid,thisscore.getSid()).eq(scannual::getCid,thisscore.getCid());
            scannual thissc=scannualMapper.selectOne(Wrapper2);
            thissc.setScore(thisscore.getScore());
            scannualMapper.update(thissc,Wrapper2);

        }

        return Result.success();
    }


    //全部上传分数并计算排名
    @PostMapping("/savescore")
    public Result<?> savescore(@RequestBody ArrayList<score> scores) {
//        System.out.println(scores);

        //保存该老师上的所有课的cid
        HashSet<String> cids=new HashSet<String>();

        //对传过来的scores数组中每一条score数据
        for (score thisscore:scores) {

            //补充设置属性
            //根据当前时间生成学期term属性
            String littleterm;
            if(new Date().getMonth()>=9||new Date().getMonth()<=2){littleterm="1";}//上半学期
            else{littleterm="2";}//下半学期
            String term=String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+littleterm;
            thisscore.setTerm(term);

            float gpa=thisscore.getCredit()*thisscore.getScore()/100;
            thisscore.setGpa(gpa);

            String state;
            if(thisscore.getScore()>=90){state="A";}
            else if(thisscore.getScore()>=80){state="B";}
            else if(thisscore.getScore()>=60){state="C";}
            else {state="D";}
            thisscore.setState(state);

            thisscore.setIsupload(true);//保存当前这条分数

            //如果这门课还没打过分，在score数据库中不存在
            Wrapper<score> Wrapper=Wrappers.<score>lambdaQuery().eq(score::getTid,thisscore.getTid()).eq(score::getSid,thisscore.getSid()).eq(score::getCid,thisscore.getCid());
            if(scoreMapper.selectOne(Wrapper)==null){
                scoreMapper.insert(thisscore);
            }
            else{//否则更新
                scoreMapper.update(thisscore,Wrapper);
            }

            //在scannual补充该学生这门课的score
            Wrapper<scannual> Wrapper2=Wrappers.<scannual>lambdaQuery().eq(scannual::getSid,thisscore.getSid()).eq(scannual::getCid,thisscore.getCid());
            scannual thissc=scannualMapper.selectOne(Wrapper2);
            thissc.setScore(thisscore.getScore());
            scannualMapper.update(thissc,Wrapper2);

            cids.add(thisscore.getCid());
        }

        //计算排名rank
        String tid=scores.get(0).getTid();

        //对该老师的每一门课计算所有学生的排名并插入或更新
        for(String cid:cids){
            scoreMapper.computerank(tid,cid);
        }

        return Result.success();
    }


}
