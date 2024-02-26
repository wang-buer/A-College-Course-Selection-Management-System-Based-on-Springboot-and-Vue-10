package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class courseinfo {
//    这个实体类是专门用于生成课表，记录学生想看到的选课详细信息用的
    private String index;
    private String cid;
    private String cname;
    private float credit;
    private String time;
    private String classroom;
    private String tname;
}
