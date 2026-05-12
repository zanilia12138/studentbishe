package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.common.UploadConstants;
import com.health.communicate.entity.Post;
import com.health.communicate.util.ImageUploadUtils;
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
import java.util.Map;
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
        String extension = ImageUploadUtils.resolveImageExtension(file);
        if (extension == null) {
            return Result.error("仅支持 jpg、jpeg、png、gif、webp 格式（若从相册选择仍失败，请换带后缀的图片或截图后再选）");
        }
        try {
            File root = new File(uploadPath).getAbsoluteFile();
            if (!root.exists() && !root.mkdirs()) {
                return Result.error("无法创建上传根目录：" + root.getAbsolutePath() + "。请检查 application.yml 中 file.upload.path（磁盘是否存在、是否有写权限）。");
            }
            File picDir = new File(root, UploadConstants.REL_LOADS_PIC);
            if (!picDir.exists() && !picDir.mkdirs()) {
                return Result.error("无法创建图片目录：" + picDir.getAbsolutePath());
            }
            if (!picDir.isDirectory()) {
                return Result.error("图片目录不可用：" + picDir.getAbsolutePath());
            }
            String newFilename = UUID.randomUUID().toString() + extension;
            File destFile = new File(picDir, newFilename).getAbsoluteFile();
            file.transferTo(destFile);
            return Result.success(UploadConstants.urlPic(newFilename));
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        } catch (Exception e) {
            return Result.error("图片上传异常：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deletePost(@PathVariable Integer id) {
        boolean ok = postService.removeById(id);
        return Result.success(ok);
    }
}