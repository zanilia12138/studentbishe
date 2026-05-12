package com.health.communicate.config;

import com.health.communicate.common.UploadConstants;
import com.health.communicate.entity.Material;
import com.health.communicate.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 将旧版 material「/uploads/文件名」迁入 {@link FileUploadProperties#getDocPath()} 根目录，并更新 file_path。
 * 源文件在 {@link FileUploadProperties#getLegacyPath()} 下查找。
 */
@Component
@Order(100)
@ConditionalOnProperty(name = "file.upload.migrate-legacy-materials", havingValue = "true")
public class LegacyMaterialFileMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LegacyMaterialFileMigration.class);
    private static final String URL_PREFIX = "/uploads/";

    private final MaterialService materialService;
    private final FileUploadProperties fileUploadProperties;

    public LegacyMaterialFileMigration(MaterialService materialService, FileUploadProperties fileUploadProperties) {
        this.materialService = materialService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        File docRoot = fileUploadProperties.docRoot();
        File legacyRoot = fileUploadProperties.legacyRoot();
        if (!docRoot.exists() && !docRoot.mkdirs()) {
            log.error("无法创建资料根目录，跳过迁移：{}", docRoot.getAbsolutePath());
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

            File src = new File(legacyRoot, rel.replace("/", File.separator));
            File dest = new File(docRoot, baseName);

            try {
                if (dest.exists() && dest.isFile()) {
                    if (!newUrl.equals(fp)) {
                        m.setFilePath(newUrl);
                        materialService.updateById(m);
                        updatedOnly++;
                        log.info("资料 id={} 文件已在 doc 根目录，已更新路径: {}", m.getId(), newUrl);
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
