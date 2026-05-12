package com.health.communicate;

import com.health.communicate.config.FileUploadProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileUploadProperties.class)
@MapperScan("com.health.communicate.mapper")
public class CommunicateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunicateApplication.class, args);
        System.out.println("启动成功");
    }
}