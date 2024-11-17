package com.example.adminsystem.service;

import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.entity.MenuApi;
import com.example.adminsystem.entity.ResMenu;
import org.springframework.stereotype.Component;

import java.util.List;

public interface MenuApiService {
    MenuApi getMenuApiById(Integer id);
    Boolean addMenuApi(MenuApi menuApi);
    List<ResMenu> resMenuApi(Menu menu);

    void delMenuApi(Integer id);
    void delMenuApi(MenuApi menuApi);
    Boolean updateMenuApi(ResMenu resMenu);
}
