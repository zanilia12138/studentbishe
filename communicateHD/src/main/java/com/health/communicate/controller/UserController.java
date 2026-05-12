package com.health.communicate.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.communicate.common.Result;
import com.health.communicate.entity.User;
import com.health.communicate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 分页查询用户列表（管理员）
    @GetMapping("/list")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> result = userService.page(page, new LambdaQueryWrapper<>());
        return Result.success(result);
    }

    // 更新用户状态
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(@RequestBody User user) {
        boolean success = userService.updateById(user);
        return Result.success(success);
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        boolean success = userService.removeById(id);
        return Result.success(success);
    }
}