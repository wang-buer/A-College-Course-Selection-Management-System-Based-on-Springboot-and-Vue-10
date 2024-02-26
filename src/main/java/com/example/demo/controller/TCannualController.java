package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.Result;
import com.example.demo.entity.*;
import com.example.demo.entity.tcannual;
import com.example.demo.entity.tcannual;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.mapper.TCannualMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController //声明该controller返回json
@RequestMapping("/TCannual") //定义该接口统一路由
public class TCannualController {

    //    将mapper引入controller
    @Resource
    TCannualMapper TCannualMapper;
    @Resource
    CourseMapper courseMapper;

    @PostMapping("/{tid}/{tname}")
//    实现老师的录入课程功能
    public Result<?> courseadding(@RequestBody tcannual tcannual,
                                    @PathVariable String tid,
                                    @PathVariable String tname) {
        tcannual.setTid(tid);
        tcannual.setTname(tname);
        TCannualMapper.insert(tcannual);

        //好像前端已经调用了course的接口处理这件事了
//        if(courseMapper.selectById(tcannual.getCid())!=null)//如果cid已存在则报错
//        {return Result.error("-11","该cid已被占用！");}
//        else {
//            course thiscourse = new course();
//            thiscourse.setCid(tcannual.getCid());
//            thiscourse.setCname(tcannual.getCname());
//            thiscourse.setCredit(tcannual.getCredit());
//
//            //根据当前时间生成学期term属性
//            String littleterm;
//            if(new Date().getMonth()>=9||new Date().getMonth()<=2){littleterm="1";}//上半学期
//            else{littleterm="2";}//下半学期
//            String term=String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+littleterm;
//            thiscourse.setTerm(term);
//            courseMapper.insert(thiscourse);
//        }

        return Result.success();
    }

    @PutMapping
//    实现老师的录入课程功能
    public Result<?> courseupdating(@RequestBody tcannual tc) {
        Wrapper<tcannual> Wrapper=Wrappers.<tcannual>lambdaQuery().eq(tcannual::getTid,tc.getTid()).eq(tcannual::getCid,tc.getCid());
        System.out.println(tc);
        TCannualMapper.update(tc,Wrapper);
//        TCannualMapper.updateById(tc);
        return Result.success();
    }

    @DeleteMapping("/{tid}/{cid}")
    //    实现老师的删课功能 下面两种方法都能实现
    public Result<?> delete(@PathVariable String tid,
                            @PathVariable String cid) {
        Map<String, Object> Map = new HashMap<String, Object>();
        Map.put("tid", tid);
        Map.put("cid", cid);
        int result= TCannualMapper.deleteByMap(Map);
//        System.out.println("影响了"+result+"行");
        return Result.success();

//        //如果不存在该记录则报错
//        System.out.println(tid+" "+cid);
//        Wrapper<tcannual> Wrapper=Wrappers.<tcannual>lambdaQuery().eq(tcannual::getTid,tid).eq(tcannual::getCid,cid);
//        if(TCannualMapper.selectOne(Wrapper) == null){
//            System.out.println("正常情况下，您应当永远也不会看到这条信息 "+tid+"您未开设该课程！"+cid);
//            return Result.error("-10","您未开设该课程！");
//        }
//        //否则删除
//        else {
//            TCannualMapper.delete(Wrapper);
//            return Result.success();
//        }

    }

    //学生选课表时所有课表
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String select) {
        LambdaQueryWrapper<tcannual> wrapper = Wrappers.<tcannual>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if(select.equals("cid")) {
                wrapper.eq(tcannual::getCid, search);
            }
            if(select.equals("cname")) {
                wrapper.like(tcannual::getCname, search);
            }
        }
        Page<tcannual> tcannualPage = TCannualMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(tcannualPage);
    }

    //教师录入课程时自己的所有课程
    @GetMapping("/{tid}")
    public Result<?> loadTCannual(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "") String select,
                                  @PathVariable String tid) {
        LambdaQueryWrapper<tcannual> wrapper = Wrappers.<tcannual>lambdaQuery();
        wrapper.eq(tcannual::getTid, tid);
        if (StrUtil.isNotBlank(search)) {
            if(select.equals("cid")) {
                wrapper.eq(tcannual::getCid, search);
            }
            if(select.equals("cname")) {
                wrapper.like(tcannual::getCname, search);
            }
            if(select.equals("credit")) {
                wrapper.like(tcannual::getCredit, search);
            }
            if(select.equals("time")) {
                wrapper.like(tcannual::getTime, search);
            }
            if(select.equals("classroom")) {
                wrapper.like(tcannual::getClassroom, search);
            }
        }
        Page<tcannual> tcannualPage = TCannualMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(tcannualPage);
    }

    @GetMapping("/coursetable/{tid}")
    //    实现学生主页的课表展示功能
    public Result<?> display(@PathVariable String tid) {
        //先用sc接收从tcannual查询到的信息，再用temp分裂转化成String二维数组，然后再转化成前台tabledata要的形式JSON对象result
        List sc= TCannualMapper.load(tid);
        ArrayList<ArrayList<courseinfo>> timetable=new ArrayList<>(5);  //外层是课时，内层是星期
//        初始化
        for(int i=0;i<5;++i){//行属性，即课时数
            ArrayList<courseinfo> thisarray = new ArrayList<courseinfo>() {{
                for(int j=0;j<8;++j) {//列属性，即第一列的课时以及后面的7个星期数
                    add(null);
                }
            }};
            timetable.add(thisarray);
        }

        int length=sc.size();
        String[][] temp=new String[length+1][5];

//      temp[1~length]为该老师教的每一门课
        temp[0][0]=length+"";     //temp[0][0]记录长度
//      对于查询到的每一个结果
        for(int i=1;i<=length;++i){
            String thissc=sc.get(i-1).toString();//转化为字符串
            String[] courseinfos=thissc.split(",|\\(|\\)");//将json格式的数据取出关键部分放入String数组

//            for(int x=0;x<9;++x)
//            {System.out.println(courseinfos[x]);}

//            0:teacher_course;1:tid=...;2:tname=...
//            3cid;4cname;5semester;6credit;7time;8classroom
            temp[i][0]=courseinfos[3].split("=")[1];//cname
            temp[i][1]=courseinfos[4].split("=")[1];//cname
            temp[i][2]=courseinfos[6].split("=")[1];//credit
            temp[i][3]=courseinfos[7].split("=")[1];//time
            temp[i][4]=courseinfos[8].split("=")[1];//classroom
        }

//      将temp中每一门课的详细信息按时间划分，放入timetable

        if(sc==null){
            return Result.error("-3","未查询到已选课程，请确认您已选课");
        }
        else{

            for(int i=1;i<=length;++i){
                courseinfo thisinfo=new courseinfo();
                thisinfo.setCid(temp[i][0]);
                thisinfo.setCname(temp[i][1]);
                thisinfo.setCredit(Float.parseFloat(temp[i][2]));
                thisinfo.setTime(temp[i][3]);
                thisinfo.setClassroom(temp[i][4]);

//                System.out.println("thisinfo:"+thisinfo);

//              将thisinfo添加在应在的位置
                String[] time =temp[i][3].split(";");//将当前课程的time按;拆分形成time数组

                //对每一个sc的每一个时间
                for (int j=0;j<time.length;++j){
//                    System.out.println("time[j]:"+time[j]);
                    //拆分时间的课时，index为编号，num为在数组中的位置
                    String index=""+time[j].charAt(2)+time[j].charAt(3)+time[j].charAt(4);
                    thisinfo.setIndex(index);
                    int num=(time[j].charAt(2)-'0')/2+1;
                    //         星期数          课时数
                    timetable.get(num).set(time[j].charAt(0)-'0', thisinfo);


                }
            }
        }

        return Result.success(timetable);
    }















}
