package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.AuthorityMapper;
import com.example.adminsystem.dao.MenuApiMapper;
import com.example.adminsystem.entity.Authority;
import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.entity.MenuApi;
import com.example.adminsystem.entity.ResMenu;
import com.example.adminsystem.service.AuthorityService;
import com.example.adminsystem.service.MenuApiService;
import com.example.adminsystem.service.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuApiServiceImpl implements MenuApiService {
    @Autowired
    private MenuApiMapper menuApiMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private AuthorityService authorityService;

    @Override
    public MenuApi getMenuApiById(Integer id) {
        return menuApiMapper.selectOne(new QueryWrapper<MenuApi>().eq("id", id));
    }

    @Override
    public Boolean addMenuApi(MenuApi menuApi) {
        menuApiMapper.insert(menuApi);
        return true;
    }

    @Override
    public List<ResMenu> resMenuApi(Menu menu) {
        List<MenuApi> menuApiList = menuApiMapper.selectList(new QueryWrapper<MenuApi>().in("mid", menu.getId()));
        List<ResMenu> resMenuList = new ArrayList<>();
        for (MenuApi menuApi:menuApiList) {
            String auth = authorityService.getAuthName(menuApi.getAid());
            resMenuList.add(new ResMenu(menuApi.getId(),menuApi.getName(),null,null,auth
                    ,menu.getPname(),"api",menuApi.getSystemDel(),null));
        }
        return resMenuList;
    }

    @Override
    public void delMenuApi(Integer id) {
        menuApiMapper.deleteById(id);
    }

    @Override
    public void delMenuApi(MenuApi menuApi) {
        menuApiMapper.deleteById(menuApi);
    }

    @Override
    public Boolean updateMenuApi(ResMenu resMenu) {
        MenuApi menuApi = menuApiMapper.selectOne(new QueryWrapper<MenuApi>().eq("id", resMenu.getId()));
        menuApi.setSystemDel(resMenu.getSystemDel());
        menuApi.setName(resMenu.getName());
        menuApiMapper.updateById(menuApi);
        Authority auth = authorityMapper.selectOne(new QueryWrapper<Authority>().eq("id", menuApi.getAid()));
        auth.setAname(resMenu.getAuth());
        authorityMapper.updateById(auth);
        return null;
    }
}
