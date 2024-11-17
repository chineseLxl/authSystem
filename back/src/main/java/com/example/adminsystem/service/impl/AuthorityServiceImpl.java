package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.AuthorityMapper;
import com.example.adminsystem.entity.Authority;
import com.example.adminsystem.entity.Catalogue;
import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.entity.ResMenu;
import com.example.adminsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


// 只进行关于权限表的操作
@Component
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public List<Authority> getAuthorityAll() {
        List<Authority> authorityList = authorityMapper.selectList(null);
        return authorityList;
    }

    public List<Integer> getAuthIds() {
        List<Authority> authorityList = authorityMapper.selectList(null);
        List<Integer> authIdList = new ArrayList<>();
        for (Authority authority: authorityList) {
            authIdList.add(authority.getId());
        }
        return authIdList;
    }

    @Override
    public Boolean isAuthorityExist(String authority) {
        Authority auth = authorityMapper.selectOne(new QueryWrapper<Authority>().eq("aname", authority));
        if (auth != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getAuthName(Integer aid) {
        Authority authority = authorityMapper.selectOne(new QueryWrapper<Authority>().eq("id", aid));
        return authority.getAname();
    }


    @Override
    public Integer addAuth(Authority auth) {
        authorityMapper.insert(auth);
        return auth.getId();
    }

    @Override
    public void delAuthById(Integer aid) {
        authorityMapper.delete(new QueryWrapper<Authority>().eq("id", aid));
    }


}
