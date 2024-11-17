package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.AuthorityMapper;
import com.example.adminsystem.dao.MenuMapper;
import com.example.adminsystem.dao.RoleMapper;
import com.example.adminsystem.entity.Authority;
import com.example.adminsystem.entity.Catalogue;
import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.entity.ResMenu;
import com.example.adminsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private MenuApiService menuApiService;

    @Override
    public Menu getMenu(Integer id) {
        return menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", id));
    }

    @Override
    public void delMenu(Integer id) {
        menuMapper.delete(new QueryWrapper<Menu>().eq("id", id));
    }

    @Override
    public List<Menu> getMenuList() {
        return menuMapper.selectList(null);
    }

    @Override
    public List<Menu> getMenuListByFid(Integer fid) {
        return menuMapper.selectList(new QueryWrapper<Menu>().eq("fid", fid));
    }

    @Override
    public List<ResMenu> getResMenuByFid(Catalogue catalogue, Integer fid) {
        List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().in("fid", fid));
        List<ResMenu> resMenuList = new ArrayList<>();
        for (Menu menu: menuList) {
                ResMenu resMenu = new ResMenu(menu.getId(),menu.getPname(),menu.getPath()
                        ,menu.getComponent(), authorityService.getAuthName((Integer) menu.getAid()),
                        catalogue.getCname(), "page", menu.getSystemDel(), menuApiService.resMenuApi(menu));
                resMenuList.add(resMenu);
        }
        return resMenuList;
    }

    @Override
    public List<Menu> getMenuListByFid(Integer fid, String[] selectStr, String type) {
        return menuMapper.selectList(new QueryWrapper<Menu>().select(selectStr).eq("fid", fid));
    }

    @Override
    public Boolean isPathExist(String path) {
        if (menuMapper.selectOne(new QueryWrapper<Menu>().eq("path", path)) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isNameExist(String name) {
        if (menuMapper.selectOne(new QueryWrapper<Menu>().eq("pname", name)) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String addMenu(Menu menu) {
        String msg = "创建成功";
        try {
            if (isNameExist(menu.getPname())) {
                msg = "菜单名称已存在";
                return msg;
            }
            if (isPathExist(menu.getPath())) {
                msg = "菜单路径已存在";
                return msg;
            }
            menuMapper.insert(menu);
            return msg;
        } catch (Exception e) {
            return "服务器出错，请联系管理员 \n（" + e + "）";
        }
    }

    @Override
    public List<Catalogue> getCatalogue() {
        return null;
    }

    @Override
    public Boolean updateMenu(ResMenu resMenu) {
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", resMenu.getId()));
        menu.setSystemDel(resMenu.getSystemDel());
        menu.setComponent(resMenu.getComponent());
        menu.setPath(resMenu.getPath());
        menuMapper.updateById(menu);
        Authority auth = authorityMapper.selectOne(new QueryWrapper<Authority>().eq("id",menu.getAid()));
        auth.setAname(resMenu.getAuth());
        authorityMapper.updateById(auth);
        return null;
    }


}
