package com.health.communicate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.communicate.entity.Post;
import com.health.communicate.mapper.PostMapper;
import com.health.communicate.service.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
}