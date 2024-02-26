package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@TableName("teacher")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class teacher {
//    定义主键account自增
    @TableId(value="tid",type = IdType.AUTO)

    private String tid;
    private String password;
    private String tname;
    private String idnum;   //身份证号

    private Date birthday;
    private Integer age;
    private String sex;
    private String phone;
    private String email;
    private String address;

    private Integer yft;     //years of teaching 从教年限
}
