package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("deadline")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class deadline {
//    @TableId(value="deadline",type = IdType.AUTO)

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//这个注释是为了将util.Date转化为中国时区。否则他会自动转化成标准时区，扣8个小时.后台到前台的时间格式转换
    private String deadline;

}
