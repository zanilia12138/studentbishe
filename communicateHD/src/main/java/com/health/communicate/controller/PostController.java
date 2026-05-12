package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.entity.Post;
import com.health.communicate.mapper.PostMapper;
import com.health.communicate.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    // 获取帖子详情
    @GetMapping("/detail/{id}")
    public Result<Post> getDetail(@PathVariable Integer id) {
        Post post = postService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        return Result.success(post);
    }

    // 获取帖子列表（带发布人昵称）
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getList() {
        List<Map<String, Object>> list = postMapper.selectPostsWithUserName();
        return Result.success(list);
    }

    // 发布帖子
    @PostMapping("/add")
    public Result<Boolean> addPost(@RequestBody Post post) {
        post.setCreateTime(LocalDateTime.now());
        boolean success = postService.save(post);
        return Result.success(success);
    }
}