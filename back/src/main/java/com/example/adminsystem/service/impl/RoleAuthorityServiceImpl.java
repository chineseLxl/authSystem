package com.example.adminsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.dao.AuthorityMapper;
import com.example.adminsystem.dao.RoleAuthorityMapper;
import com.example.adminsystem.entity.Authority;
import com.example.adminsystem.entity.Catalogue;
import com.example.adminsystem.entity.ResMenu;
import com.example.adminsystem.entity.RoleAuthority;
import com.example.adminsystem.service.AuthorityService;
import com.example.adminsystem.service.CatalogueService;
import com.example.adminsystem.service.MenuService;
import com.example.adminsystem.service.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleAuthorityServiceImpl implements RoleAuthorityService {
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private CatalogueService catalogueService;
    @Autowired
    private MenuService menuService;

    @Override
    public void delRoleAuth(Integer aid) {
        roleAuthorityMapper.delete(new QueryWrapper<RoleAuthority>().eq("aid", aid));
    }

    @Override
    public List<String> getAuthorityByRid(Integer rid) {
        List<RoleAuthority> roleAuthorityList = roleAuthorityMapper
                .selectList(new QueryWrapper<RoleAuthority>().eq("rid", rid).select("aid"));
        List<String> authList = new ArrayList<>();
        // 根据权限id获取权限名
        for (RoleAuthority roleAuthority: roleAuthorityList) {
            Authority authority = authorityMapper
                    .selectOne(new QueryWrapper<Authority>().eq("id", roleAuthority.getAid()));
            authList.add(authority.getAname());
        }
        return authList;
    }

    @Override
    public List<Integer> getAuthIds(Integer rid) {
        List<RoleAuthority> ids = roleAuthorityMapper.selectList(new QueryWrapper<RoleAuthority>().eq("rid",rid));
        List<Integer> idList = new ArrayList<>();
        for(RoleAuthority roleAuthority: ids) {
            idList.add(roleAuthority.getAid());
        }
        return idList;
    }

    @Override
    public void addAdminAuth(Integer aid) {
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRid(1);
        roleAuthority.setAid(aid);
        roleAuthorityMapper.insert(roleAuthority);
    }

    public List<Catalogue> getRoleAuthRouter(Integer rid, String[] strings) {
        // 根据角色id 获取权限id List
        List<Integer> aids = getAuthIds(rid);
        List<Catalogue> catalogueList = catalogueService.getCatalogueByAids(aids);
        for (Catalogue catalogue: catalogueList) {
            catalogue.setPageList(menuService.getMenuListByFid(catalogue.getId(),strings,"page"));
            catalogue.setId(null);
            catalogue.setAid(null);
        }
        return catalogueList;
    }

    //获取权限对应的路由
    @Override
    public List<Catalogue> getRoleAuthRouter(Integer rid) {
        // 根据角色id 获取权限id List
        List<Integer> aids = getAuthIds(rid);
        List<Catalogue> catalogueList = catalogueService.getCatalogueByAids(aids);
        for (Catalogue catalogue: catalogueList) {
            catalogue.setPageList(menuService.getMenuListByFid(catalogue.getId()));
            catalogue.setId(null);
            catalogue.setAid(null);
        }
        return catalogueList;
    }

    @Override
    public Object getRoleAuthMenus(Integer rid) {
        // 根据角色id 获取权限id List
        List<Integer> aids = getAuthIds(rid);
        List<Catalogue> catalogueList = catalogueService.getCatalogueByAids(aids);
        List<ResMenu> resMenuList = new ArrayList<>();
        for (Catalogue catalogue: catalogueList) {
            List<ResMenu> menuList = menuService.getResMenuByFid(catalogue, catalogue.getId());
            ResMenu resMenu = new ResMenu(catalogue.getId(),catalogue.getCname(),catalogue.getPath()
                    ,null,authorityService.getAuthName(catalogue.getAid()),
                    null,"catalogue", catalogue.getSystemDel(), menuList);

            resMenuList.add(resMenu);
        }
        System.out.println(resMenuList);
        return resMenuList;
    }
}
