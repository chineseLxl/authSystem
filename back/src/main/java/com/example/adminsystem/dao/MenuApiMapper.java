package com.example.adminsystem.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.entity.MenuApi;
import org.springframework.stereotype.Repository;

@Repository(value = "menuApiMapper")
public interface MenuApiMapper extends BaseMapper<MenuApi> {
}
