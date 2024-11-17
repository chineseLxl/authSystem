package com.example.adminsystem.entity;

import lombok.Data;

@Data
public class ReqMenu {
    private Integer catalogueMenu;
    private String auth;
    private String htmlFileName;
    private String menuName;
    private String menuPath;
    private Integer radio;
    private String type;
    private Integer systemDel;
}