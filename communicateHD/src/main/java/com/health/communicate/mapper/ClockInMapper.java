package com.health.communicate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.communicate.entity.ClockIn;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ClockInMapper extends BaseMapper<ClockIn> {
    // 联表查询打卡记录+用户名
    List<Map<String, Object>> selectClockInWithUserName(@Param("userId") Integer userId);
}