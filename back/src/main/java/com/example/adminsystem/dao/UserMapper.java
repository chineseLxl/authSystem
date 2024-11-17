package com.example.adminsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.User;
import org.springframework.stereotype.Repository;

@Repository(value ="userMapper")
public interface UserMapper extends BaseMapper<User> {

}

