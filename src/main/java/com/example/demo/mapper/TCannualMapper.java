package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.student_course;
import com.example.demo.entity.tcannual;
import com.example.demo.entity.teacher_course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TCannualMapper extends BaseMapper<tcannual> {

    @Select("select * from tcannual where tid=#{tid}")
    List<teacher_course> load(String tid);

}
