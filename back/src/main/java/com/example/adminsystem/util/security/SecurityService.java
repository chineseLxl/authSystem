package com.example.adminsystem.util.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.UserMapper;
import com.example.adminsystem.dao.UserRoleMapper;
import com.example.adminsystem.entity.User;
import com.example.adminsystem.entity.UserRole;
import com.example.adminsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<UserRole> userRole = userRoleMapper.selectList(new QueryWrapper<UserRole>().in("user_id", user.getId()));
        List<Integer> userIds = new ArrayList<>();
        for (UserRole ur: userRole) {
            userIds.add(ur.getRid());
        }
        user.setRoles(roleService.getNowUserRole(userIds));
        return user;
    }
}

