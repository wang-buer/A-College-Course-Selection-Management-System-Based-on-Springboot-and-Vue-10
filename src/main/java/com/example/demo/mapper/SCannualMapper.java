package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.student_course;
import com.example.demo.entity.scannual;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SCannualMapper extends BaseMapper<scannual> {

    @Select("select * from scannual where sid=#{sid}")
    List<student_course> load(String sid);

}
