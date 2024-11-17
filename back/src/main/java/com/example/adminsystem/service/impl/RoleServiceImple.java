package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.RoleMapper;
import com.example.adminsystem.entity.Role;
import com.example.adminsystem.entity.UserRole;
import com.example.adminsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleServiceImple implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    // 获取权限列表
    @Override
    public List<String> getRoles() {
        List<String> roleList = new ArrayList<>();
        for(Role role: roleMapper.selectList(null)) {
            roleList.add(role.getName());
        }
        return roleList;
    }

    @Override
    public Integer getRid(String role) {
        Role r = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", role));
        if (r != null) {
            return r.getId();
        } else {
            return 0;
        }

    }

    @Override
    public List<String> getNowUserRole(List<Integer> roleIds) {
        List<String> roleList = new ArrayList<>();
        List<Role> roles = roleMapper.selectList(new QueryWrapper<Role>().in("id",roleIds));
        for (Role role:roles) {
            roleList.add(role.getName());
        }
        return roleList;
    }

}
