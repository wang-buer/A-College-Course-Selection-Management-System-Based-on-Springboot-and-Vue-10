package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.student_course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SCMapper extends BaseMapper<student_course> {

    @Select("select * from sctable where sid=#{sid}")
    List<student_course> load(String sid);

}
