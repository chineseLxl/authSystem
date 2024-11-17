package com.example.adminsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User implements UserDetails {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @TableField(value = "username", condition = SqlCondition.LIKE)
    private String username;

    @TableField(value = "password", condition = SqlCondition.LIKE)
    private String password;
    @TableField(value = "system_del")
    private String systemDel;
    @TableField(value = "system_exit")
    private String systemExit;
//    private String role;
    @TableField(exist = false)
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String r : roles){
            authorities.add(new SimpleGrantedAuthority(r));
        }
        return authorities;
    }

    //是否 没过期？
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //是否 没锁定？
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否 可用？
    @Override
    public boolean isEnabled(){
        return true;
    }

}

