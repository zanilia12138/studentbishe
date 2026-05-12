package com.health.communicate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.communicate.entity.Announcement;
import com.health.communicate.mapper.AnnouncementMapper;
import com.health.communicate.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
}