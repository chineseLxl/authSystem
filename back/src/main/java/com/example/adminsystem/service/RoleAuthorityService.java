package com.example.adminsystem.service;

import com.example.adminsystem.entity.Catalogue;
import com.example.adminsystem.entity.RoleAuthority;

import java.util.List;

public interface RoleAuthorityService {
    void delRoleAuth(Integer aid);
    List<String> getAuthorityByRid(Integer rid);
    List<Integer> getAuthIds(Integer rid);
    void addAdminAuth(Integer aid);
    // 动态路由获取
    List<Catalogue> getRoleAuthRouter(Integer rid, String[] strings);
    List<Catalogue> getRoleAuthRouter(Integer rid);
    Object getRoleAuthMenus(Integer rid);
}
