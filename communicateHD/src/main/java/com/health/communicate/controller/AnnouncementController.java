package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.entity.Announcement;
import com.health.communicate.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // 获取公告列表
    @GetMapping("/list")
    public Result<List<Announcement>> getList() {
        List<Announcement> list = announcementService.lambdaQuery()
                .orderByDesc(Announcement::getCreateTime)
                .list();
        return Result.success(list);
    }

    // 发布公告
    @PostMapping("/publish")
    public Result<String> publishAnnouncement(@RequestBody Announcement announcement) {
        announcement.setCreateTime(LocalDateTime.now());
        announcementService.save(announcement);
        return Result.success("公告发布成功");
    }
}