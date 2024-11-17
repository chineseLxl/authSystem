package com.example.adminsystem.service.impl;

import com.example.adminsystem.dao.AuthorityMapper;
import com.example.adminsystem.service.AuthorityService;
import com.example.adminsystem.service.CatalogueService;
import com.example.adminsystem.service.RedisService;
import com.example.adminsystem.service.UtilService;
import com.example.adminsystem.util.JsonResult;

import com.example.adminsystem.util.TokenUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UtilServiceImpl implements UtilService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TokenUtil tokenUtil;
    @Value("${html.file}")
    private String filePath;
    @Autowired
    private RedisService redisService;
    @Override
    public JsonResult decodeToken(String token) {
        tokenUtil.parseToken(token);
        return new JsonResult<>(tokenUtil.parseToken(token), "token解析");
    }

    @Override
    public JsonResult loadHtml() {
        File file = new File(filePath);
        File[] files = file.listFiles();
        List<String> fileList = new ArrayList<>();
        for (File f: files) {
            fileList.add(f.getName());
        }
        return new JsonResult<>(fileList, "1");
    }

    // 启动时将所有权限标识存入redis
    @Override
    @PostConstruct
    public void PostConstruc() {
        redisTemplate.opsForValue().set("test", "启动时redis测试");
        redisService.updateAuth();
        System.out.println(redisTemplate.opsForHash().entries("catalogue"));
        System.out.println(redisTemplate.opsForHash().entries("menu"));
        System.out.println("启动时构造:" + redisTemplate.opsForValue().get("test"));
    }

}
