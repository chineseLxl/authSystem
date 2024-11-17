package com.example.adminsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.Authority;
import org.springframework.stereotype.Repository;

@Repository(value = "authorityMapper")
public interface AuthorityMapper extends BaseMapper<Authority> {
}
