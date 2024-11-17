package com.example.adminsystem.util.security;

import com.example.adminsystem.dao.AuthorityMapper;
import com.example.adminsystem.entity.NowAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 权限判断
@Service("aps")
public class AuthorityPermissionService {
    @Autowired
    private NowAuthority nowAuthority;
    public Boolean isAuthorityExist(String authority) {
        System.out.println("当前权限列表：" + nowAuthority.getRoleList());
        if (nowAuthority.getRoleList().contains(authority)) {
            return true;
        } else {
            return false;
        }

    }
}
