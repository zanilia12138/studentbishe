package com.health.communicate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties fileUploadProperties;

    public WebMvcConfig(FileUploadProperties fileUploadProperties) {
        this.fileUploadProperties = fileUploadProperties;
    }

    private static String dirUri(File dir) {
        String u = dir.getAbsoluteFile().toURI().toString();
        return u.endsWith("/") ? u : u + "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 先注册更具体的路径，再注册 /uploads/** 兜底旧文件
        registry.addResourceHandler("/uploads/loads/doc/**")
                .addResourceLocations(dirUri(fileUploadProperties.docRoot()));
        registry.addResourceHandler("/uploads/loads/pic/**")
                .addResourceLocations(dirUri(fileUploadProperties.picRoot()));
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(dirUri(fileUploadProperties.legacyRoot()));
    }
}
