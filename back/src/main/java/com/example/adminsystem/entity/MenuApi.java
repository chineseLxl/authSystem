package com.example.adminsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("menu_api")
public class MenuApi {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer mid;
    private Integer aid;
//    @TableField(value = "system_del")
    private Integer systemDel;
    private Date time;
}
