package com.example.adminsystem.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Data
@TableName("menu")
@Component
public class Menu<T> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "path")
    private String path;
    private String pname;
    private String component;
    private String meta;
    private String type;
    private T fid;
    private Integer aid;
    @TableField(value = "system_del")
    private Integer systemDel;
    private Date time;
}
