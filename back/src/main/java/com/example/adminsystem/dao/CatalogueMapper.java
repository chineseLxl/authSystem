package com.example.adminsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminsystem.entity.Catalogue;
import org.springframework.stereotype.Repository;

@Repository(value = "CatalogueMapper")
public interface CatalogueMapper extends BaseMapper<Catalogue> {
}
