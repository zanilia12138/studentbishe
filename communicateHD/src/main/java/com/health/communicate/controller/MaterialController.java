package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.common.UploadConstants;
import com.health.communicate.config.FileUploadProperties;
import com.health.communicate.entity.Material;
import com.health.communicate.mapper.MaterialMapper;
import com.health.communicate.service.MaterialService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @GetMapping("/list")
    public Result<List<Material>> getList(@RequestParam(required = false) String keyword) {
        List<Material> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = materialMapper.selectMaterialsWithUserNameByTitle(keyword.trim());
        } else {
            list = materialMapper.selectMaterialsWithUserName();
        }
        return Result.success(list);
    }

    @PostMapping("/upload")
    public Result<Material> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("userId") Integer userId) {

        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        try {
            File docRoot = fileUploadProperties.docRoot();
            if (!docRoot.exists() && !docRoot.mkdirs()) {
                return Result.error("无法创建资料根目录：" + docRoot.getAbsolutePath() + "。请检查 file.upload.doc-path。");
            }
            if (!docRoot.isDirectory()) {
                return Result.error("资料根目录不可用：" + docRoot.getAbsolutePath());
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            File destFile = new File(docRoot, newFilename).getAbsoluteFile();
            file.transferTo(destFile);

            Material material = new Material();
            material.setTitle(title);
            material.setCategory(category);
            material.setUserId(userId);
            material.setFilePath(UploadConstants.urlDoc(newFilename));
            material.setCreateTime(LocalDateTime.now());
            material.setDownloadCount(0);

            boolean success = materialService.save(material);
            if (success) {
                return Result.success(material);
            } else {
                destFile.delete();
                return Result.error("上传失败");
            }

        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable Integer id, HttpServletResponse response) {
        Material material = materialService.getById(id);
        if (material == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String filePath = material.getFilePath();
        if (filePath == null || filePath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = fileUploadProperties.resolveDiskFile(filePath.trim());
        if (file == null || !file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            materialMapper.incrementDownloadCount(id);
        } catch (Exception ignore) {
        }

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            response.setContentType("application/octet-stream");
            String encodedFilename = URLEncoder.encode(material.getTitle(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename);
            response.setContentLengthLong(file.length());

            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (IOException e) {
        }
    }
}
