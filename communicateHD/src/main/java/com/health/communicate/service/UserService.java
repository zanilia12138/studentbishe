package com.health.communicate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.communicate.entity.User;
import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(String username, String password);
}