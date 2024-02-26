package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("student")//与数据库关联
@Data   //mybatis-plus lombok注解,帮助自动生成get和set方法
public class student {
//    定义主键uid自增
    @TableId(value="sid",type = IdType.AUTO)

   //身份证号有时以x结尾，因此应用String而不是Integer
    //身份证和手机号位数过多，超过6位(long类型为64位，即6位10进制数)，因此只能用String，然后用isNumeric判断
    private String sid;     //学号
    private String password;
    private String sname;
    private String idnum;   //身份证号

    private String specialty;   //专业
    private Date enterdate;   //入学日期
    private Date gradudate;   //毕业日期
    private String grade;       //年级
    private String classes;       //班级

    private Date birthday;
    private Integer age;
    private String sex;
    private String phone;
    private String email;
    private String address;

    private String hassecpro;

    private String secretproq1; //密保问题
    private String secretproa1;
    private String secretproq2;
    private String secretproa2;
}
