package com.health.communicate.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Set;

/**
 * 帖子 / 打卡等图片上传：扩展名与 MIME 推断（避免无后缀 blob、部分浏览器文件名异常导致误判）。
 */
public final class ImageUploadUtils {

    public static final Set<String> ALLOWED_IMAGE_EXT = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private ImageUploadUtils() {
    }

    /**
     * @return 小写扩展名（含点），如 ".png"；无法识别则返回 null
     */
    public static String resolveImageExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase(Locale.ROOT);
        }
        if (ALLOWED_IMAGE_EXT.contains(extension)) {
            return extension;
        }
        String ct = file.getContentType();
        if (ct != null) {
            String mime = ct.toLowerCase(Locale.ROOT);
            if (mime.contains("jpeg")) {
                return ".jpg";
            }
            if (mime.contains("png")) {
                return ".png";
            }
            if (mime.contains("gif")) {
                return ".gif";
            }
            if (mime.contains("webp")) {
                return ".webp";
            }
        }
        return null;
    }
}
