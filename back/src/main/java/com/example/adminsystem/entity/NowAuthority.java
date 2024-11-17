package com.example.adminsystem.entity;

import lombok.Data;

import java.util.List;

@Data
public class NowAuthority {
    List<Integer> aidList;
    List<String> roleList;
    String role;
}
