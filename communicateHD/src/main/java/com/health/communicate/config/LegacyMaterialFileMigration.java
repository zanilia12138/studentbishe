package com.health.communicate.config;

import com.health.communicate.common.UploadConstants;
import com.health.communicate.entity.Material;
import com.health.communicate.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * 将旧版「资料」文件从上传根目录（/uploads/xxx）迁入 loads/doc，并更新 material.file_path。
 * 在 application.yml 中设置 {@code file.upload.migrate-legacy-materials: true} 后启动<strong>一次</strong>，完成后请改回 false。
 */
@Component
@Order(100)
@ConditionalOnProperty(name = "file.upload.migrate-legacy-materials", havingValue = "true")
public class LegacyMaterialFileMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LegacyMaterialFileMigration.class);
    private static final String URL_PREFIX = "/uploads/";

    private final MaterialService materialService;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    public LegacyMaterialFileMigration(MaterialService materialService) {
        this.materialService = materialService;
    }

    @Override
    public void run(ApplicationArguments args) {
        File root = new File(uploadPath).getAbsoluteFile();
        File docDir = new File(root, UploadConstants.REL_LOADS_DOC);
        if (!docDir.exists() && !docDir.mkdirs()) {
            log.error("无法创建资料目录，跳过迁移：{}", docDir.getAbsolutePath());
            return;
        }

        List<Material> all = materialService.list();
        int moved = 0;
        int updatedOnly = 0;
        int skipped = 0;
        int failed = 0;

        for (Material m : all) {
            String fp = m.getFilePath();
            if (fp == null || fp.isBlank()) {
                skipped++;
                continue;
            }
            fp = fp.trim();
            if (!fp.startsWith(URL_PREFIX) || fp.startsWith(URL_PREFIX + "loads/")) {
                skipped++;
                continue;
            }

            String rel = fp.substring(URL_PREFIX.length());
            if (rel.isEmpty() || rel.endsWith("/")) {
                skipped++;
                continue;
            }

            String baseName = new File(rel).getName();
            if (baseName.isEmpty()) {
                skipped++;
                continue;
            }

            String newUrl = UploadConstants.urlDoc(baseName);

            File src = new File(root, rel.replace("/", File.separator));
            File dest = new File(docDir, baseName);

            try {
                if (dest.exists() && dest.isFile()) {
                    if (!newUrl.equals(fp)) {
                        m.setFilePath(newUrl);
                        materialService.updateById(m);
                        updatedOnly++;
                        log.info("资料 id={} 文件已在 doc 目录，已更新路径: {}", m.getId(), newUrl);
                    } else {
                        skipped++;
                    }
                    continue;
                }

                if (src.exists() && src.isFile()) {
                    Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    m.setFilePath(newUrl);
                    materialService.updateById(m);
                    moved++;
                    log.info("资料 id={} 已迁移: {} -> {}", m.getId(), fp, newUrl);
                } else {
                    log.warn("资料 id={} 源文件不存在，跳过: {}", m.getId(), src.getAbsolutePath());
                    failed++;
                }
            } catch (Exception e) {
                log.error("资料 id={} 迁移失败: {}", m.getId(), e.getMessage());
                failed++;
            }
        }

        log.info("资料路径迁移结束：移动文件={}, 仅更新库={}, 跳过={}, 失败={}", moved, updatedOnly, skipped, failed);
    }
}
