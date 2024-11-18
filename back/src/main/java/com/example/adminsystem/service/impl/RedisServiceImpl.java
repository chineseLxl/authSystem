package com.example.adminsystem.service.impl;

import com.example.adminsystem.entity.Menu;
import com.example.adminsystem.service.AuthorityService;
import com.example.adminsystem.service.CatalogueService;
import com.example.adminsystem.service.MenuService;
import com.example.adminsystem.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisServiceImpl implements RedisService {
//    @Autowired
//    private CatalogueService catalogueService;
//    @Autowired
//    private AuthorityService authorityService;
//    @Autowired
//    private MenuService menuService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

//    @Override
//    public Boolean updateAuth() {
//        try {
//            // 储存目录标识
//            List<Integer> aidList = catalogueService.getCatalogueAids();
//            Map<String,String> map = new HashMap<>();
//            for (Integer aid: aidList) {
//                map.put(aid.toString(), authorityService.getAuthName(aid));
//            }
//            // 储存菜单标识
//            List<Menu> menuList = menuService.getMenuList();
//            Map<String,String> menuMap = new HashMap<>();
//            for (Menu menu: menuList) {
////                menuMap.put(menu.getPath(), authorityService.getAuthName((Integer) menu.getAuth()));
//            }
//
//            redisTemplate.opsForHash().putAll("menu", menuMap);
//            redisTemplate.opsForHash().putAll("catalogue", map);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public Boolean getAuthName(String path) {
        System.out.println(redisTemplate.opsForHash().get("menu",path));
        return true;
    }
    @Override
    public void toBlackList(String jwt) {
        redisTemplate.opsForHash().put("jwtBlackList", jwt, "1");
        // 设置hashkey过期时间
        redisTemplate.opsForHash().getOperations().expire(jwt,1,TimeUnit.HOURS);
    }
    @Override
    public Boolean isBlacklistExist(String jwt) {
        try {
            if (redisTemplate.opsForHash().get("jwtBlackList", jwt) != null) {
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
    }


}
