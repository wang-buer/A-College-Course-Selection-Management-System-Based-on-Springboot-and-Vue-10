package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("course")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class course {
//    定义主键account自增
    @TableId(value="cid",type = IdType.AUTO)

    private String cid;
    private String cname;

    private float credit;     //学分
    private String term;


}
