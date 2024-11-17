package com.example.adminsystem.service;

import com.example.adminsystem.util.JsonResult;


public interface UtilService {
    public JsonResult decodeToken(String token);
    JsonResult loadHtml();

    void PostConstruc();

}
