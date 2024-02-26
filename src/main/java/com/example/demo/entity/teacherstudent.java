package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("teacherstudent")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class teacherstudent {

    private String tid;
    private String sid;
    private String cid;
    private String sname;

    private Boolean checkin1;
    private Boolean checkin2;
    private Boolean checkin3;
    private Boolean checkin4;
    private Boolean checkin5;
    private Boolean checkin6;
    private Boolean checkin7;
    private Boolean checkin8;
    private Boolean checkin9;
    private Boolean checkin10;
    private Boolean checkin11;
    private Boolean checkin12;
    private Boolean checkin13;
    private Boolean checkin14;
    private Boolean checkin15;
    private Boolean checkin16;
    private Boolean checkin17;
    private Boolean checkin18;



}
