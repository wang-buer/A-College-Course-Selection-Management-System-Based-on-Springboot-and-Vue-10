package com.example.demo.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;
import java.util.Date;

@TableName("log")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class log {
//    定义主键account自增
    @TableId(value="id",type = IdType.AUTO)

    private String id;
    private String type;

//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")//这个注释是为了将util.Date转化为中国时区。否则他会自动转化成标准时区，扣8个小时
    private Timestamp logintime;     //


}
