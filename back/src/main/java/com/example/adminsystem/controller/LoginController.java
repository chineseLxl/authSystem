package com.example.adminsystem.controller;

import com.example.adminsystem.entity.User;
import com.example.adminsystem.service.*;
import com.example.adminsystem.util.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class LoginController {
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

    @PutMapping("/login")
    public JsonResult login(@RequestBody User user){
        AuthenticationManager authenticationManager = null;
        System.out.println(user.getUsername() + "," + user.getPassword());
        try {
            authenticationManager = configuration.getAuthenticationManager();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(token);
            //这一步与上面的作用一致
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
            roleHierarchy.setHierarchy(hierarchy);
            // 角色列表
            List<String> roleList = new ArrayList<>();
            for (GrantedAuthority s: roleHierarchy.getReachableGrantedAuthorities(authenticate.getAuthorities())) {
                roleList.add(s.getAuthority());
            }
            this.roleList = roleList;
            return userService.login(user.getUsername(), user.getPassword(), authenticate.getName()); //登录成功应该返回json，包含jwt等信息，为了简单，这些都省去了。
//            return userService.login(user.getUsername(), user.getPassword(), roleList); //登录成功应该返回json，包含jwt等信息，为了简单，这些都省去了。
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("登录失败", "-1");
        }
    }
}
