package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.*;
import com.example.adminsystem.entity.*;
import com.example.adminsystem.service.*;
import com.example.adminsystem.util.JsonResult;
import com.example.adminsystem.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class UserServiceImpl implements UserService {
    private class ResJson {
        private List<Menu> route;
        private String token;

        private ResJson(List<Menu> route, String token) {
            this.route = route;
            this.token = token;
        }


        public List<Menu> getRoute() {
            return route;
        }

        public String getToken() {
            return token;
        }

    }

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RoleAuthorityService roleAuthorityService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public JsonResult getUserInfo() {
        return null;
    }

    @Override
    public JsonResult login(String username, String password, String role) {
        Integer uid = userMapper.selectOne(new QueryWrapper<User>().eq("username", role)).getId();
        // 获取角色id
        Integer rid = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("user_id", uid)).getRid();
        String roleName = roleMapper.selectOne(new QueryWrapper<Role>().eq("id", rid)).getName();
        Map<String,Object> jsonMap = new HashMap<>();
        String jwt = tokenUtil.getToken(rid.toString(),username,roleName);
        String redisJwt = (String) redisTemplate.opsForHash().get("loginUserList", username);
        if (redisJwt == null) {
            redisTemplate.opsForHash().put("loginUserList", username, jwt);
        } else {
            redisTemplate.opsForHash().put("jwtBlackList",redisJwt,"1");
            redisTemplate.opsForHash().getOperations().expire(redisJwt,1, TimeUnit.HOURS);
            redisTemplate.opsForHash().put("loginUserList", username, jwt);
        }
        jsonMap.put("jwt", jwt);
        String[] strings = {"path", "pname", "component", "meta"};
        List<Catalogue> catalogueList = roleAuthorityService.getRoleAuthRouter(rid, strings);
        jsonMap.put("routers", catalogueList);
        return new JsonResult<>(jsonMap, true);
    }

    @Override
    public String getUsername(Integer uid) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("id", uid)).getUsername();
    }

    @Override
    public List<User> getUserAll() {
        return userMapper.selectList(null);
    }

    @Override
    public List<UserInfo> getLoginUser(List<String> wsUserName) {
        return null;
    }

    @Override
    public List<Integer> getUserRoleId(Integer uid) {
        List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>().in("user_id", uid));
        List<Integer> roleIds = new ArrayList<>();
        if (!userRoles.isEmpty()) {
            for (UserRole userRole:userRoles) {
                roleIds.add(userRole.getRid());
            }
        }
        return roleIds;
    }

    @Override
    public List<String> getUserRole(Integer uid) {
        List<Integer> roleIds = getUserRoleId(uid);
        List<String> roleNames = new ArrayList<>();
        if (!roleIds.isEmpty()) {
            List<Role> roleList = roleMapper.selectList(new QueryWrapper<Role>().in("id", roleIds));
            for (Role role:roleList) {
                roleNames.add(role.getName());
            }
        }
        return roleNames;
    }

    @Override
    public List<Integer> getUserAid(Integer uid) {
        List<Integer> aidList = new ArrayList<>();
        List<Integer> roleIds = getUserRoleId(uid);
        List<RoleAuthority> authList = roleAuthorityMapper.selectList(
                new QueryWrapper<RoleAuthority>().in("rid", roleIds));
        if (!authList.isEmpty()) {
            for (RoleAuthority roleAuthority:authList) {
                aidList.add(roleAuthority.getAid());
            }
        }
        return aidList;
    }

    @Override
    public List<String> getUserAuth(Integer uid) {
        List<String> aNameList = new ArrayList<>();
        List<Integer> authIds = getUserAid(uid);
        if (!authIds.isEmpty()) {
            List<Authority> authList = authorityMapper.selectList(new QueryWrapper<Authority>().in("id", authIds));
            for (Authority authority:authList) {
                aNameList.add(authority.getAname());
            }
        }
        return aNameList;
    }

}
