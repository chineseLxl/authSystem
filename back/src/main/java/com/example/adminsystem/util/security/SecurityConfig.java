package com.example.adminsystem.util.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.adminsystem.util.TokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private CustomAuthorizationManager customAuthorizationManager;
    @Autowired
    WebApplicationContext applicationContext;
    @Value("${role.hierarchy}")
    private String hierarchy;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                return bCryptPasswordEncoder.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
            }
        };
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request, response, authException) -> {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("需要登陆");
        };
    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }

    @Component
    public class SecurityFilter extends GenericFilterBean {
        @Resource
        SecurityService security;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//            String jwt = request.getParameter("jwt");//正常情况下，jwt应当放在header里面，示例为了简单直接放在请求参数了
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            System.out.println("是否是login：" + httpServletRequest.getRequestURI().equals("/login"));
            // 跳过login
            if (httpServletRequest.getRequestURI().equals("/login")) {
                chain.doFilter(request,response);
                return;
            }
            try {
                String jwt = httpServletRequest.getHeader("jwt");
                if (jwt == null) {
                    jwt = httpServletRequest.getHeader("Sec-WebSocket-Protocol");
                }
                Map<String,String> jwtMap = tokenUtil.parseToken(jwt);
                UserDetails user = security.loadUserByUsername(jwtMap.get("userName"));
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                //这一步是核心，将验证信息放入到SecurityContext，表明已经认证成功
                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (JWTDecodeException | NullPointerException e) {
                System.out.println("jwt错误，认证失败");
                System.out.println(e);
            }
            chain.doFilter(request, response);
        }
    }

    /**
     * 加载账号密码json登录
     */
//    @Bean
//    JsonLoginFilter myJsonLoginFilter(HttpSecurity http) throws Exception {
//        JsonLoginFilter myJsonLoginFilter = new JsonLoginFilter();
//        //自定义登录url
//        myJsonLoginFilter.setFilterProcessesUrl("/login");
//        myJsonLoginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
//        myJsonLoginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
//        myJsonLoginFilter.setAuthenticationManager(authenticationManager(http));
//        return myJsonLoginFilter;
//    }

    /**
     * 获取标有注解 AnonymousAccess 的访问路径
     */

    private String[] getAnonymousUrls() {
        // 获取所有的 RequestMapping
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext
                .getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> allAnonymousAccess = new HashSet<>();
        // 循环 RequestMapping
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
            HandlerMethod value = infoEntry.getValue();
            // 获取方法上 AnonymousAccess 类型的注解
            AnonymousAccess methodAnnotation = value.getMethodAnnotation(AnonymousAccess.class);
            // 如果方法上标注了 AnonymousAccess 注解，就获取该方法的访问全路径
            if (methodAnnotation != null) {
                allAnonymousAccess.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        return allAnonymousAccess.toArray(new String[0]);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .formLogin(form -> form.disable())//禁用默认登录页面1
                .logout(config->config.disable())//禁用默认登出页面
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//禁用session，前																												后端分离不需要
                .httpBasic(httpBasic -> httpBasic.disable())//
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/login", "/logout", "/authorityTest").anonymous();
                            auth.requestMatchers(getAnonymousUrls()).anonymous();
                            // 自定义权限过滤
                            auth.anyRequest().access(customAuthorizationManager);
                            // 除设置外的路由角色验证
//                            auth.anyRequest().hasAuthority("STAFF");
                        }
//
                )//设置权限，除了登录登出不需要认证，其余均需要认证
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)//添加JWT的处理过滤器，用于从JWT中解析																												用户信息
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint()))//自定义用户认证失败的处理器，否则的话，当用户未认证的情况下，浏览器会直接报出403异常，我们需要的是json提示信息，供前端处理，设置前后效果如下图所示
                .build();
    }
}

