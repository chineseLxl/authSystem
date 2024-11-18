package com.example.adminsystem.service;

import org.springframework.data.redis.core.RedisTemplate;

public interface RedisService {
    // 更新权限标识
//    Boolean updateAuth();
    Boolean getAuthName(String path);
    Boolean isBlacklistExist(String jwt);
    void toBlackList(String jwt);
}
