package com.example.adminsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.UserRole;
import org.springframework.stereotype.Repository;

@Repository(value ="userRoleMapper")
public interface UserRoleMapper extends BaseMapper<UserRole> {

}

