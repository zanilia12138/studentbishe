package com.health.communicate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer userId;
    private LocalDateTime createTime;
}