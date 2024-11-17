package com.example.adminsystem.controller;

import com.example.adminsystem.service.*;
import com.example.adminsystem.util.JsonResult;
import com.example.adminsystem.util.security.AnonymousAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/test", method = {RequestMethod.POST,RequestMethod.GET,})
public class TestController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private RoleAuthorityService authorityService;
    @GetMapping("/he")
    public String he(){
        return "请求的资源";// 表示受保护的资源
    }
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisService redisService;

    @AnonymousAccess
    @PostMapping("/test")
    public JsonResult apiTest() {
        System.out.println("test测试！！！");
        return null;
    }

    @Autowired
    private CatalogueService catalogueService;
    @AnonymousAccess
    @PostMapping("/testRedis")
    public JsonResult testRedis() {
        System.out.println(redisTemplate.opsForHash().entries("catalogue"));
        System.out.println(redisTemplate.opsForHash().entries("menu"));
        redisService.getAuthName("system/user");
        return new JsonResult<>(redisTemplate.opsForValue().get("test"), "1");
    }

    @Autowired
    private MenuService menu2Service;

    @AnonymousAccess
    @PostMapping("/testMenu")
    public JsonResult testMenu() {
        authorityService.getRoleAuthRouter(1);
        return new JsonResult(authorityService.getRoleAuthRouter(1), true);
    }

    @AnonymousAccess
    @PostMapping("/getMenus")
    public JsonResult getMenus() {
        return new JsonResult<>();
    }
}
