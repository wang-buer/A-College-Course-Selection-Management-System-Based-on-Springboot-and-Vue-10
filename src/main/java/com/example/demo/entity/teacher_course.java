package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tctable")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class teacher_course {
//    定义主键account自增
    @TableId(value="tid",type = IdType.AUTO)
    private String tid;
    private String tname;

    private String cid;
    private String cname;

    private String term;        //学期
    private float credit;     //学分

    private String time;        //上课时间，由英文逗号分隔的多值属性
    private String classroom;

}
