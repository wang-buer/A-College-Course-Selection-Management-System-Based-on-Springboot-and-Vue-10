package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sctable")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class student_course {
//    定义主键account自增
    @TableId(value="sid",type = IdType.AUTO)
    private String sid;
    private String sname;

    private String cid;
    private String cname;

    private String term;        //学期 例如：大三上
    private float credit;     //学分

    private String time;        //上课时间，由英文逗号分隔的多值属性
    private String classroom;

    private String tid;        //上课教师
    private String tname;

}
