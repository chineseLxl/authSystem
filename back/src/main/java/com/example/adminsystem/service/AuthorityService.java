package com.example.adminsystem.service;

import com.example.adminsystem.entity.Authority;
import com.example.adminsystem.entity.Catalogue;

import java.util.List;

// 只进行关于权限表的操作
public interface AuthorityService {
    List<Authority> getAuthorityAll();
    Boolean isAuthorityExist(String authority);
    List<Integer> getAuthIds();
    String getAuthName(Integer aid);
    Integer addAuth(Authority auth);
    void delAuthById(Integer aid);
}
