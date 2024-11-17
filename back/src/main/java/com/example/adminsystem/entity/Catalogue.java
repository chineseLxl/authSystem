package com.example.adminsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@TableName("catalogue")
public class Catalogue {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String cname;
    private String path;
    private Integer aid;
    @TableField(exist = false)
    private List<Menu> pageList;
//    @TableField(value = "system_del")
    private Integer systemDel;
    private Date time;
}
