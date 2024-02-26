package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.Result;
import com.example.demo.entity.courseinfo;
import com.example.demo.entity.scannual;
import com.example.demo.mapper.SCannualMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController //声明该controller返回json
@RequestMapping("/SCannual") //定义该接口统一路由
public class SCannualController {

    //    将mapper引入controller
    @Resource
    SCannualMapper SCannualMapper;
    
    @PostMapping("/choosecourse/{sid}/{sname}")
//    实现学生的选课功能
    public Result<?> scannualchoosing(@PathVariable String sid,
                                      @PathVariable String sname,
                                      @RequestBody scannual sc) {
        //如果今年已选这门课则报错
        System.out.println(sc);
        //如果已存在该记录则报错
        if(SCannualMapper.selectOne(Wrappers.<scannual>lambdaQuery().eq(scannual::getSid,sid).eq(scannual::getCid,sc.getCid())) != null){
            System.out.println(sc.getCname()+"您已选过该课程");
            return Result.error("-6","您已选过该课程");
        }
        //再检测是否有时间冲突
        else {
            System.out.println("检查是否有时间冲突");
            //如果时间安排有冲突报错
            boolean conflict=false;
            LambdaQueryWrapper<scannual> wrapper = Wrappers.<scannual>lambdaQuery();
            wrapper.eq(scannual::getSid, sid);

            String[] times=sc.getTime().split(";");
            String 冲突课程="";

            //对当前课程的每一个时间，依次检测有无冲突
            for(String i:times) {
                wrapper.like(scannual::getTime, i);

                if(SCannualMapper.selectCount(wrapper)!=0){
                    conflict=true;
                    for(int x=0;x<SCannualMapper.selectCount(wrapper);++x){
                        冲突课程+=SCannualMapper.selectList(wrapper).get(x).getCname()+" ";
                    }
                }
            }

            if(conflict){
                System.out.println("与"+冲突课程+"有时间冲突");
                return Result.error("-9","与"+冲突课程+"有时间冲突!");
            }
            else {//否则插入
                sc.setSid(sid);
                sc.setSname(sname);

                SCannualMapper.insert(sc);
                return Result.success();
            }
        }

    }

    @DeleteMapping("/cancelcourse/{sid}")
    //    实现学生的退课功能
    public Result<?> delete(@PathVariable String sid,
                            @RequestBody scannual sc) {
        //如果不存在该记录则报错
        System.out.println(sc);
        Wrapper<scannual> Wrapper=Wrappers.<scannual>lambdaQuery().eq(scannual::getSid,sid).eq(scannual::getCid,sc.getCid());
        if(SCannualMapper.selectOne(Wrapper) == null){
            System.out.println("正常情况下，您应当永远也不会看到这条信息 "+sc.getCname()+"您未选报该课程！");
            return Result.error("-8","您未选报该课程！");
        }
        //否则删除
        else {
            SCannualMapper.delete(Wrapper);
            return Result.success();
        }
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String sid) {
        LambdaQueryWrapper<scannual> wrapper = Wrappers.<scannual>lambdaQuery();
        wrapper.eq(scannual::getSid, sid);
        Page<scannual> scannualPage = SCannualMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(scannualPage);
    }


    //这里解对象的方法是比较暴力的.tostring.split，实际上用对象自带的.get获取属性会好很多
    @GetMapping("/{sid}")
    //    实现学生主页的课表展示功能
    public Result<?> display(@PathVariable String sid) {
        //先用sc接收从scannual查询到的信息，再用temp分裂转化成String二维数组，然后再转化成前台tabledata要的形式JSON对象result
        List sc= SCannualMapper.load(sid);
        ArrayList<ArrayList<courseinfo>> timetable=new ArrayList<>(5);  //外层是课时，内层是星期
//        初始化
        for(int i=0;i<5;++i){
            ArrayList<courseinfo> thisarray = new ArrayList<courseinfo>() {{
                for(int j=0;j<8;++j) {
                    add(null);
                }
            }};
            timetable.add(thisarray);
        }

        int length=sc.size();
        String[][] temp=new String[length+1][5];

//      temp[1~length]为该学生选的每一门课
        temp[0][0]=length+"";     //temp[0][0]记录长度
//      对于查询到的每一个结果
        for(int i=1;i<=length;++i){
            String thissc=sc.get(i-1).toString();//转化为字符串
            String[] courseinfos=thissc.split(",|\\(|\\)");//将json格式的数据取出关键部分放入String数组

//            for(int x=0;x<10;++x)
//            {System.out.println(courseinfos[x]);}

//            0:student_course;1:sid=...;2:sname=...
//            3cid;4cname;5semester;6credit;7time;8classroom;9tid;10tname
            temp[i][0]=courseinfos[4].split("=")[1];//cname
            temp[i][1]=courseinfos[6].split("=")[1];//credit
            temp[i][2]=courseinfos[7].split("=")[1];//time
            temp[i][3]=courseinfos[8].split("=")[1];//classroom
            temp[i][4]=courseinfos[10].split("=")[1];//tname
        }

//      将temp中每一门课的详细信息按时间划分，放入timetable

        if(sc==null){
            return Result.error("-3","未查询到已选课程，请确认您已选课");
        }
        else{

            for(int i=1;i<=length;++i){
                courseinfo thisinfo=new courseinfo();
                thisinfo.setCname(temp[i][0]);
                thisinfo.setCredit(Float.parseFloat(temp[i][1]));
                thisinfo.setTime(temp[i][2]);
                thisinfo.setClassroom(temp[i][3]);
                thisinfo.setTname(temp[i][4]);

//                System.out.println("thisinfo:"+thisinfo);

//              将thisinfo添加在应在的位置
                String[] time =temp[i][2].split(";");//将当前课程的time按;拆分形成time数组

                //对每一个sc的每一个时间
                for (int j=0;j<time.length;++j){

                    //拆分时间的课时，index为编号，num为在数组中的位置
                    String index=""+time[j].charAt(2)+time[j].charAt(3)+time[j].charAt(4);
                    thisinfo.setIndex(index);
                    int num=(time[j].charAt(2)-'0')/2+1;
                    //          课时数         星期数
                    timetable.get(num-1).set(time[j].charAt(0)-'0', thisinfo);


                }
            }
        }

        return Result.success(timetable);
    }
















}
