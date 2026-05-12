package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.common.UploadConstants;
import com.health.communicate.config.FileUploadProperties;
import com.health.communicate.entity.ClockIn;
import com.health.communicate.util.ImageUploadUtils;
import com.health.communicate.mapper.ClockInMapper;
import com.health.communicate.service.ClockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/clock")
public class ClockInController {

    @Autowired
    private ClockInService clockInService;

    @Autowired
    private ClockInMapper clockInMapper;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    // 获取用户打卡记录（带用户名）
    @GetMapping("/list/{userId}")
    public Result<List<Map<String, Object>>> getList(@PathVariable Integer userId) {
        List<Map<String, Object>> list = clockInMapper.selectClockInWithUserName(userId);
        return Result.success(list);
    }

    // 提交打卡（可选 imageUrl，由 /uploadImage 先上传得到）
    @PostMapping("/add")
    public Result<Boolean> addClock(@RequestBody ClockIn clockIn) {
        clockIn.setCreateTime(LocalDateTime.now());
        boolean success = clockInService.save(clockIn);
        return Result.success(success);
    }

    // 打卡配图上传，返回保存到 image_url 的相对路径
    @PostMapping("/uploadImage")
    public Result<String> uploadClockImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择图片文件");
        }
        String extension = ImageUploadUtils.resolveImageExtension(file);
        if (extension == null) {
            return Result.error("仅支持 jpg、jpeg、png、gif、webp 格式（若从相册选择仍失败，请换带后缀的图片或截图后再选）");
        }

        try {
            File picRoot = fileUploadProperties.picRoot();
            if (!picRoot.exists() && !picRoot.mkdirs()) {
                return Result.error("无法创建图片根目录：" + picRoot.getAbsolutePath() + "。请检查 file.upload.pic-path。");
            }
            if (!picRoot.isDirectory()) {
                return Result.error("图片根目录不可用：" + picRoot.getAbsolutePath());
            }

            String newFilename = UUID.randomUUID().toString() + extension;
            File destFile = new File(picRoot, newFilename).getAbsoluteFile();
            file.transferTo(destFile);

            String url = UploadConstants.urlPic(newFilename);
            return Result.success(url);
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        } catch (Exception e) {
            return Result.error("图片上传异常：" + e.getMessage());
        }
    }
}