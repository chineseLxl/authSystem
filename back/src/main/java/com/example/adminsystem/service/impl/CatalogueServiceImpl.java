package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.*;
import com.example.adminsystem.entity.*;
import com.example.adminsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogueServiceImpl implements CatalogueService {
    @Autowired
    private CatalogueMapper catalogueMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuApiMapper menuApiMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public Catalogue getCatalogue(Integer id) {
        return catalogueMapper.selectOne(new QueryWrapper<Catalogue>().eq("id", id));
    }

    @Override
    public List<Integer> getCatalogueAids() {
        // 返回菜单目录对应的权限标识
        List<Integer> idList = new ArrayList<>();
        List<Catalogue> catalogueList = catalogueMapper.selectList(null);
        for (Catalogue c: catalogueList) {
            idList.add(c.getAid());
        }
        return idList;
    }

    @Override
    public List<Catalogue> getCatalogueByAids(List<Integer> aid) {
        return catalogueMapper.selectList(new QueryWrapper<Catalogue>().in("aid", aid));
    }

    @Override
    public List<Catalogue> getCatalogueList() {
        return catalogueMapper.selectList(null);
    }

    @Override
    public Boolean isPathExist(String path) {
        if (catalogueMapper.selectOne(new QueryWrapper<Catalogue>().eq("path", path)) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isNameExist(String name) {
        if (catalogueMapper.selectOne(new QueryWrapper<Catalogue>().eq("cname", name)) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean updateCatalogue(ResMenu resMenu) {
        Catalogue catalogue = catalogueMapper.selectOne(new QueryWrapper<Catalogue>().eq("id", resMenu.getId()));
        catalogue.setSystemDel(resMenu.getSystemDel());
        catalogue.setCname(resMenu.getName());
        catalogueMapper.updateById(catalogue);
        if (!resMenu.getAuth().isEmpty()) {
            Authority auth = authorityMapper.selectOne(
                    new QueryWrapper<Authority>().eq("id", catalogue.getAid()));
            auth.setAname(resMenu.getAuth());
            authorityMapper.updateById(auth);
        }

        return null;
    }

    @Override
    public String addCatalogue(Catalogue catalogue) {
        String msg = "创建成功";
        System.out.println(catalogue);
        if (isNameExist(catalogue.getCname())) {
            msg = "菜单名称已存在";
            return msg;
        }
        if (isPathExist(catalogue.getPath())) {
            msg = "菜单路径已存在";
            return msg;
        }
        catalogueMapper.insert(catalogue);

        return msg;

    }

    @Override
    public void delCatalogue(Integer id) {
        // 权限id列表
        List<Integer> authList = new ArrayList<>();
        Catalogue catalogue = catalogueMapper.selectOne(new QueryWrapper<Catalogue>().eq("id", id));
        authList.add(catalogue.getAid());
        List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().in("fid",catalogue.getId()));
        if (menuList.isEmpty()) {
            // 空目录
            System.out.println("空目录");
            // 删除角色对应权限
            roleAuthorityMapper.delete(new QueryWrapper<RoleAuthority>().in("aid", authList));
            // 删除目录
            catalogueMapper.delete(new QueryWrapper<Catalogue>().eq("id", catalogue.getId()));
            // 删除权限
            authorityMapper.delete(new QueryWrapper<Authority>().in("id", authList));
        } else {
            // 非空目录
            System.out.println("非空目录");
            // 菜单id列表
            List<Integer> menuIds = new ArrayList<>();
            for (Menu menu:menuList) {
                menuIds.add(menu.getId());
                authList.add(menu.getAid());
            }
            List<MenuApi> menuApiList = menuApiMapper.selectList(
                    new QueryWrapper<MenuApi>().in("mid", menuIds));
            if (!menuApiList.isEmpty()) {
                List<Integer> menuApiIds = new ArrayList<>();
                for (MenuApi menuApi: menuApiList) {
                    authList.add(menuApi.getAid());
                    menuApiIds.add(menuApi.getId());
                }
                // 删除角色对应权限
                roleAuthorityMapper.delete(new QueryWrapper<RoleAuthority>().in("aid", authList));
                // 删除页面事件
                menuApiMapper.delete(new QueryWrapper<MenuApi>().in("id", menuApiIds));
                // 删除页面
                menuMapper.delete(new QueryWrapper<Menu>().in("id", menuIds));
                // 删除目录
                catalogueMapper.delete(new QueryWrapper<Catalogue>().eq("id", catalogue.getId()));
                // 删除权限
                authorityMapper.delete(new QueryWrapper<Authority>().in("id", authList));
            }
        }
    }


}
