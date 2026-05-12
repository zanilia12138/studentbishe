package com.health.communicate.controller;

import com.health.communicate.common.Result;
import com.health.communicate.entity.User;
import com.health.communicate.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error("用户名或密码不能为空");
        }

        User one = userService.lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .one();

        if (one == null || !one.getPassword().equals(user.getPassword())) {
            return Result.error("账号或密码错误");
        }

        if (one.getStatus() == 0) {
            return Result.error("账号已被禁用，请联系管理员");
        }

        String token = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("role", one.getRole());
        data.put("nickname", one.getNickname());
        data.put("userId", one.getId());

        log.info("用户 [{}] 登录成功，角色：{}", user.getUsername(), one.getRole());
        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        // 1. 非空校验
        if (user.getUsername() == null || user.getPassword() == null || user.getNickname() == null) {
            return Result.error("用户名、密码、昵称不能为空");
        }

        // 2. 用户名重复校验
        Long count = userService.lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .count();
        if (count > 0) {
            return Result.error("用户名已被注册，请更换");
        }

        // 3. 赋值默认字段（前端不需要传）
        user.setRole(1);          // 默认角色：学生
        user.setStatus(1);         // 默认状态：正常
        user.setCreateTime(LocalDateTime.now()); // 注册时间

        // 4. 保存用户（实际项目建议对密码加密）
        boolean save = userService.save(user);
        if (save) {
            log.info("用户 [{}] 注册成功", user.getUsername());
            return Result.success("注册成功");
        } else {
            return Result.error("注册失败，请稍后重试");
        }
    }
}