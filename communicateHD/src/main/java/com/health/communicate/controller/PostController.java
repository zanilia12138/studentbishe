package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.entity.Post;
import com.health.communicate.mapper.PostMapper;
import com.health.communicate.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    private static final Set<String> ALLOWED_IMAGE_EXT = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

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

    @PostMapping("/uploadImage")
    public Result<String> uploadPostImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择图片文件");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase(Locale.ROOT);
        }
        if (!ALLOWED_IMAGE_EXT.contains(extension)) {
            return Result.error("仅支持 jpg、jpeg、png、gif、webp 格式");
        }
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File postDir = new File(uploadDir, "post");
            if (!postDir.exists()) {
                postDir.mkdirs();
            }
            String newFilename = UUID.randomUUID().toString() + extension;
            File destFile = new File(postDir, newFilename);
            file.transferTo(destFile);
            return Result.success("/uploads/post/" + newFilename);
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deletePost(@PathVariable Integer id) {
        boolean ok = postService.removeById(id);
        return Result.success(ok);
    }
}