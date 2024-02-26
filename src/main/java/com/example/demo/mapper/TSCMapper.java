package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.score;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TSCMapper extends BaseMapper<score> {

    @Select("select cid from tcannual where tid=#{tid}")
    List<String> getcids(String tid);
    @Select("select cname from tcannual where tid=#{tid}")
    List<String> getcnames(String tid);

    @Select("select sid from scannual where cid=#{cid}")
    List<String> getsids(String cid);
    @Select("select sname from scannual where cid=#{cid}")
    List<String> getsnames(String cid);

}
