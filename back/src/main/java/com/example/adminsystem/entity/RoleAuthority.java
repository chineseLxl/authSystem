package com.example.adminsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// 角色对应权限表
@Data
@TableName("role_authority")
public class RoleAuthority {
    private Integer rid;
    private Integer aid;
}
