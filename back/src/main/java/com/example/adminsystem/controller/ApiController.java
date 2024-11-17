package com.example.adminsystem.controller;

import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.service.*;
import com.example.adminsystem.util.JsonResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@EnableMethodSecurity(prePostEnabled = true,jsr250Enabled = true)
public class ApiController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisService redisService;
    @Value("${role.hierarchy}")
    private String hierarchy;
    private List<String> roleList;
    @Resource
    AuthenticationConfiguration configuration;

    // 路由返回测试
//    @RequestMapping(value = "/getRoute", method = {RequestMethod.GET,RequestMethod.POST})
//    public JsonResult getRouteApi(@RequestBody Map<String,Object> roleMap) {
//        System.out.println(roleMap.get("role"));
//        return new JsonResult<>(menuService.getRoute(roleMap.get("role").toString()), "路由权限");
//    }

    @PostMapping("/decodeToken")
    public JsonResult decodeTokenApi(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println(token);
        return utilService.decodeToken(token);
    }

    @PostMapping("/getRoles")
    public JsonResult getRoles() {
        return new JsonResult<>(roleService.getRoles(), "1");
    }

    @PostMapping("/loadHtmlDir")
    public JsonResult loadHtmlDir() {
        return utilService.loadHtml();
    }

    @PostMapping("/authorityTest")
    @PreAuthorize("@aps.isAuthorityExist('system:menu:view')")
    public JsonResult authorityTest() {
        return new JsonResult(authorityService.getAuthorityAll(), "1");
    }

//    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})

}
