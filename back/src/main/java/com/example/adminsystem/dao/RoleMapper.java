package com.example.adminsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.Role;
import org.springframework.stereotype.Repository;

@Repository("roleMapper")
public interface RoleMapper extends BaseMapper<Role> {

}
