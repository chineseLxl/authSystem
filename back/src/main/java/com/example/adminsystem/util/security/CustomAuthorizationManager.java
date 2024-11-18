package com.example.adminsystem.util.security;

import com.example.adminsystem.entity.NowAuthority;
import com.example.adminsystem.service.RedisService;
import com.example.adminsystem.service.RoleAuthorityService;
import com.example.adminsystem.service.RoleService;
import com.example.adminsystem.util.webSocket.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

// 自定义权限过滤
@Service
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleAuthorityService roleAuthorityService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private NowAuthority nowAuthority;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        System.out.println(object.getRequest().getHeader("jwt"));
        System.out.println(redisService.isBlacklistExist(object.getRequest().getHeader("jwt")));
        if (authentication.get().getAuthorities().toArray()[0].toString().equals("ROLE_ANONYMOUS") ||
            redisService.isBlacklistExist(object.getRequest().getHeader("jwt"))) {
            return new AuthorizationDecision(false);
        } else {
            if (nowAuthority.getRoleList() == null) {
                Integer rid = roleService.getRid(authentication.get().getAuthorities().toArray()[0].toString());
                List<String> authList = roleAuthorityService.getAuthorityByRid(rid);
                nowAuthority.setRoleList(authList);
            }
//            redisService.getAuthName(object.getRequest().getRequestURI());
            return new AuthorizationDecision(true);
        }

    }
}
