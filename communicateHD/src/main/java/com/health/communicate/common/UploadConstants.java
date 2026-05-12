package com.health.communicate.common;

/**
 * 上传目录：在 {@code file.upload.path} 下使用 loads/doc（资料含 PDF 等）、loads/pic（图片）。
 * 对外 URL 仍为 /uploads/...，与 {@link com.health.communicate.config.WebMvcConfig} 一致。
 */
public final class UploadConstants {

    private UploadConstants() {
    }

    public static final String URL_PREFIX = "/uploads";
    /** 相对上传根目录：非图片资料 */
    public static final String REL_LOADS_DOC = "loads/doc";
    /** 相对上传根目录：图片 */
    public static final String REL_LOADS_PIC = "loads/pic";

    public static String urlDoc(String filename) {
        return URL_PREFIX + "/" + REL_LOADS_DOC + "/" + filename;
    }

    public static String urlPic(String filename) {
        return URL_PREFIX + "/" + REL_LOADS_PIC + "/" + filename;
    }
}
