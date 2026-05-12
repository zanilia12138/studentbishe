package com.health.communicate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * 上传根目录拆分：资料与图片可配置到不同磁盘/路径；对外 URL 仍为 /uploads/loads/doc|pic/文件名。
 */
@Data
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    /** 学习资料等文档根目录（文件直接存放在此目录下） */
    private String docPath = "./uploads/loads/doc";
    /** 帖子、打卡等图片根目录 */
    private String picPath = "./uploads/loads/pic";
    /** 兼容旧路径：/uploads/post、/uploads/clock、以及根目录下的旧文件 */
    private String legacyPath = "./uploads";

    public File docRoot() {
        return new File(docPath).getAbsoluteFile();
    }

    public File picRoot() {
        return new File(picPath).getAbsoluteFile();
    }

    public File legacyRoot() {
        return new File(legacyPath).getAbsoluteFile();
    }

    /**
     * 将 /uploads/... 解析为本地文件；非法路径返回 null。
     */
    public File resolveDiskFile(String publicPath) {
        if (publicPath == null || !publicPath.startsWith("/uploads/")) {
            return null;
        }
        if (publicPath.contains("..")) {
            return null;
        }
        if (publicPath.startsWith("/uploads/loads/doc/")) {
            String name = publicPath.substring("/uploads/loads/doc/".length());
            if (name.isEmpty() || name.indexOf('/') >= 0) {
                return null;
            }
            return new File(docRoot(), name);
        }
        if (publicPath.startsWith("/uploads/loads/pic/")) {
            String name = publicPath.substring("/uploads/loads/pic/".length());
            if (name.isEmpty() || name.indexOf('/') >= 0) {
                return null;
            }
            return new File(picRoot(), name);
        }
        String rel = publicPath.substring("/uploads/".length());
        return new File(legacyRoot(), rel.replace('/', File.separatorChar));
    }
}
