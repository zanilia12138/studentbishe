package com.health.communicate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.communicate.entity.ClockIn;
import com.health.communicate.mapper.ClockInMapper;
import com.health.communicate.service.ClockInService;
import org.springframework.stereotype.Service;

@Service
public class ClockInServiceImpl extends ServiceImpl<ClockInMapper, ClockIn> implements ClockInService {
}