package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("score")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class score {
//    定义主键account自增
    @TableId(value="sid",type = IdType.AUTO)

    private String sid;
    private String sname;

    private String cid;
    private String cname;

    private String tid;
    private String tname;

    private String term;        //学期
    private float score;        //分数
    private float credit;     //学分
    private float gpa;        //绩点    =score/100*credit-5 或score<60则=0)

    private String state;        //绩点对应等级   ABCD

    private Integer studentrank;
    private Boolean isupload; //是否被上传，0表示暂存，1表示提交
}
