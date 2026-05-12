package com.health.communicate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.communicate.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<Map<String, Object>> selectCommentsWithDetail(@Param("postId") Integer postId);
}