package com.health.communicate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.communicate.entity.Comment;
import com.health.communicate.mapper.CommentMapper;
import com.health.communicate.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}