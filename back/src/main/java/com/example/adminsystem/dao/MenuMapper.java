package com.example.adminsystem.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.Menu;
import org.springframework.stereotype.Repository;

@Repository(value = "menuMapper")
public interface MenuMapper extends BaseMapper<Menu> {
}
