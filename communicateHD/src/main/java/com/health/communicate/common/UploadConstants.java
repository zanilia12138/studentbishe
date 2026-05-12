package com.health.communicate.common;

/**
 * 对外 URL 前缀（与 {@link WebMvcConfig} 中 ResourceHandler 一致）。
 * 资料实际根目录见 {@code file.upload.doc-path}，图片见 {@code file.upload.pic-path}。
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
