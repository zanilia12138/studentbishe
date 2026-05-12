package com.health.communicate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.health.communicate.mapper")
public class CommunicateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunicateApplication.class, args);
        System.out.println("启动成功");
    }
}