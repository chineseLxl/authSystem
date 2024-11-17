package com.example.adminsystem.service;

import com.example.adminsystem.entity.UserRole;
import com.example.adminsystem.util.JsonResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<String> getRoles();
    Integer getRid(String role);
    List<String> getNowUserRole(List<Integer> roleIds);
}
