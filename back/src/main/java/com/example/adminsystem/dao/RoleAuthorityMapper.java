package com.example.adminsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.RoleAuthority;
import org.springframework.stereotype.Repository;

@Repository("RoleAuthorityMapper")
public interface RoleAuthorityMapper extends BaseMapper<RoleAuthority> {
}
