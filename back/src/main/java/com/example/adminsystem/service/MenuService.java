package com.example.adminsystem.service;

import com.example.adminsystem.entity.Catalogue;
import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.entity.ResMenu;

import java.util.List;

public interface MenuService {
    Menu getMenu(Integer id);
    void delMenu(Integer id);
    List<Menu> getMenuList();
    List<Menu> getMenuListByFid(Integer aid);
    List<ResMenu> getResMenuByFid(Catalogue catalogue,Integer fid);
    List<Menu> getMenuListByFid(Integer fid, String[] selectStr, String type);
    Boolean isPathExist(String path);
    Boolean isNameExist(String name);
    String addMenu(Menu menu);
    List<Catalogue> getCatalogue();
    Boolean updateMenu(ResMenu resMenu);
}
