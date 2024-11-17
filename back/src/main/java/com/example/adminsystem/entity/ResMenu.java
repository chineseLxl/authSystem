package com.example.adminsystem.entity;

import lombok.Data;

import java.util.List;

@Data
public class ResMenu  {
    private Integer id;
    private String name;
    private String path;
    private String component;
    private String auth;
    private String fname;
    private String type;
    private Integer systemDel;
    private List<ResMenu> children;
    public ResMenu(Integer id, String name, String path, String component, String auth, String fname,
                   String type, Integer systemDel, List<ResMenu> children) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.component = component;
        this.auth = auth;
        this.fname = fname;
        this.type = type;
        this.children = children;
        this.systemDel = systemDel;
    }
    public ResMenu() {

    }
}
