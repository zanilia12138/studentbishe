package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.entity.Comment;
import com.health.communicate.mapper.CommentMapper;
import com.health.communicate.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    // 获取帖子评论（带帖子标题+评论人昵称）
    @GetMapping("/list/{postId}")
    public Result<List<Map<String, Object>>> getCommentList(@PathVariable Integer postId) {
        List<Map<String, Object>> list = commentMapper.selectCommentsWithDetail(postId);
        return Result.success(list);
    }

    // 新增评论
    @PostMapping("/add")
    public Result<String> addComment(@RequestBody Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        commentService.save(comment);
        return Result.success("评论成功");
    }
}