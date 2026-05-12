package com.health.communicate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.communicate.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    // 联表查询帖子列表+发布人昵称
    List<Map<String, Object>> selectPostsWithUserName();
}