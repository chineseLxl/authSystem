package com.example.adminsystem.service;

import com.example.adminsystem.entity.User;
import com.example.adminsystem.entity.UserInfo;
import com.example.adminsystem.util.JsonResult;

import java.util.List;

public interface UserService {
    public JsonResult getUserInfo();

    JsonResult login(String username, String password, String role);
    String getUsername(Integer uid);
    List<User> getUserAll();
    List<UserInfo> getLoginUser(List<String> wsUserName);
    List<Integer> getUserRoleId(Integer uid);
    List<String> getUserRole(Integer uid);
    List<Integer> getUserAid(Integer uid);
    List<String> getUserAuth(Integer uid);
}
