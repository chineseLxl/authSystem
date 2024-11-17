package com.example.adminsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_role")
public class UserRole {
    @TableField(value = "user_id")
    private Integer uid;
    @TableField(value = "role_id")
    private Integer rid;
}
