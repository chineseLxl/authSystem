package com.example.adminsystem.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UserInfo {
    private Integer id;
    private String username;
    private List<String> roles;
    private List<String> authList;
    private String status;
    private String systemDel;
    private String systemExit;
}
