package com.example.adminsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminsystem.entity.*;
import com.example.adminsystem.service.*;
import com.example.adminsystem.util.JsonResult;
import com.example.adminsystem.util.TokenUtil;
import com.example.adminsystem.util.security.AnonymousAccess;
import com.example.adminsystem.util.webSocket.MyWebSocketHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/system", method = {RequestMethod.GET,RequestMethod.POST})
public class SystemController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuApiService menuApiService;
    @Autowired
    private MenuApiService apiService;
    @Autowired
    private RoleAuthorityService roleAuthorityService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private CatalogueService catalogueService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @PostMapping("/getMenus")
    public JsonResult getMenus(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer rid = Integer.parseInt(tokenUtil.parseToken(jwt).get("userId"));
        Object resMenus = roleAuthorityService.getRoleAuthMenus(rid);
        return new JsonResult<>(resMenus, "1");
    }

    @PostMapping("/getCatalogue")
    public JsonResult getCatalogue() {
        return new JsonResult<>(catalogueService.getCatalogueList(), "1");
    }

    @PostMapping("/getPages")
    public JsonResult getMenus() {
        return new JsonResult<>(menuService.getMenuList(), "1");
    }

    @PostMapping("/addMenu")
    public JsonResult addMenu(@RequestBody ReqMenu menu) {
        System.out.println(menu);
        Integer aid;
        Authority authority = new Authority();
        String msg = "创建成功";
        if (!authorityService.isAuthorityExist(menu.getAuth())) {
            switch (menu.getRadio()) {
                // 添加目录
                case 1:
                    authority.setAname(menu.getAuth());
                    aid = authorityService.addAuth(authority);
                    Catalogue catalogue = new Catalogue();
                    catalogue.setPath(menu.getMenuPath());
                    catalogue.setCname(menu.getMenuName());
                    catalogue.setAid(aid);
                    catalogue.setSystemDel(menu.getSystemDel());
                    msg = catalogueService.addCatalogue(catalogue);
                    // 给超级管理员同步添加权限
                    roleAuthorityService.addAdminAuth(aid);
                    return new JsonResult<>(msg,"1");
                // 添加页面
                case 2:
                    authority.setAname(menu.getAuth());
                    aid = authorityService.addAuth(authority);
                    Menu menu1 = new Menu<>();
                    menu1.setPath(menu.getMenuPath());
                    menu1.setFid(menu.getCatalogueMenu());
                    menu1.setAid(aid);
                    menu1.setPname(menu.getMenuName());
                    menu1.setComponent(menu.getHtmlFileName());
                    menu1.setType("page");
                    menu1.setSystemDel(menu.getSystemDel());
                    System.out.println(menu1);
                    menuService.addMenu(menu1);
                    // 给超级管理员同步添加权限
                    roleAuthorityService.addAdminAuth(aid);
                    System.out.println(222);
                    return new JsonResult<>(msg,"1");
                // 添加按钮（页面api）
                case 3:
                    authority.setAname(menu.getAuth());
                    aid = authorityService.addAuth(authority);
                    MenuApi menuApi = new MenuApi();
                    menuApi.setAid(aid);
                    menuApi.setMid(menu.getCatalogueMenu());
                    menuApi.setName(menu.getMenuName());
                    menuApi.setSystemDel(menu.getSystemDel());
                    apiService.addMenuApi(menuApi);
                    // 给超级管理员同步添加权限
                    roleAuthorityService.addAdminAuth(aid);
                    System.out.println(333);
                    return new JsonResult<>(msg,"1");
                default:
                    return new JsonResult<>("-1","服务器出错");
            }
        } else {
            return new JsonResult<>("该权限标识已存在", "-1");
        }
    }

    @PreAuthorize("@aps.isAuthorityExist('system:menu:remove')")
    @PostMapping("/delMenu")
    public JsonResult delMenu(@RequestBody Map<String,String> reqMap) {
        Integer id = Integer.parseInt(reqMap.get("id"));
        if (reqMap.get("type").equals("api")) {
            MenuApi menuApi = menuApiService.getMenuApiById(id);
            // 页面事件
            menuApiService.delMenuApi(id);
            roleAuthorityService.delRoleAuth(menuApi.getAid());
            authorityService.delAuthById(menuApi.getAid());
        } else if (reqMap.get("type").equals("page")){
            Menu menu = menuService.getMenu(id);
            // 页面
            menuService.delMenu(id);
            roleAuthorityService.delRoleAuth(menu.getAid());
            authorityService.delAuthById(menu.getAid());
        } else if (reqMap.get("type").equals("catalogue")){
            catalogueService.delCatalogue(id);
        } else {
            return new JsonResult<>("出错","-1");
        }
        System.out.println(reqMap.get("id"));
        return new JsonResult<>("操作成功","1");
    }

    @PostMapping("/updateMenu")
    public JsonResult updateMenu(@RequestBody ResMenu resMenu) {
        System.out.println(resMenu);
        switch (resMenu.getType()) {
            case "catalogue":
                catalogueService.updateCatalogue(resMenu);
                break;
            case "page":
                menuService.updateMenu(resMenu);
                break;
            case "api":
                menuApiService.updateMenuApi(resMenu);
                break;
        }
        return new JsonResult<>();
    }

    @PostMapping("/getLoginUserInfo")
    public JsonResult getLoginUserInfo() throws IOException {
//        webSocketHandler.sendUserMessage("admin", "发送消息测试");
        Map<String, WebSocketSession> webSocketSessionMap = webSocketHandler.getAllSessions();
        System.out.println(webSocketSessionMap);
        List<User> users = userService.getUserAll();
        List<UserInfo> userInfoList = new ArrayList<>();
        for (User user:users) {
            UserInfo userInfo = new UserInfo();
            if (webSocketHandler.isUserLogin(user.getUsername())) {
                userInfo.setStatus("1");
            } else {
                userInfo.setStatus("0");
            }
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setRoles(userService.getUserRole(user.getId()));
            userInfo.setAuthList(userService.getUserAuth(user.getId()));
            userInfo.setSystemDel(user.getSystemDel());
            userInfo.setSystemExit(user.getSystemExit());
            userInfoList.add(userInfo);
        }
//        System.out.println(webSocketSessionMap);
        return new JsonResult<>(userInfoList,"1");
    }
    @PostMapping("/outLoginUser")
    public JsonResult outLoginUser(@RequestBody Map<String,Object> reqMap) throws IOException {
        System.out.println(reqMap);
        webSocketHandler.exitWebSocket((String) reqMap.get("username"));
        return new JsonResult<>();
    }
}
