package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.score;
import com.example.demo.entity.student_course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ScoreMapper extends BaseMapper<score> {

    @Select("select * from tcannual,scannual where tcannual.tid=#{tid}&&tcannual.cid=scannual.cid")
    List<score> loadscore(String tid);

    @Select("update score,(select rank() over(order by score) as studentrank,sid " +
                        "from score " +
                        "where tid=#{tid} && cid=#{cid}) as thisrank " +
            "set score.studentrank=thisrank.studentrank " +
            "where tid=#{tid} && cid=#{cid} && score.sid=thisrank.sid")
    List<score> computerank(String tid,String cid);

}
