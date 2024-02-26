package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("scannual")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class scannual {
//    定义主键account自增
    @TableId(value="sid",type = IdType.AUTO)
    private String sid;
    private String sname;
    private String cid;
    private String cname;
    private String credit;
    private String time;        //上课时间，由英文逗号分隔的多值属性
    private String classroom;
    private String tid;        //上课时间，由英文逗号分隔的多值属性
    private String tname;
    private float score;
}
