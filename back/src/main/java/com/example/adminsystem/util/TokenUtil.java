package com.example.adminsystem.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

@Component
public class TokenUtil {
    @Value("${token.secretKey}")
    private String secretKey;
    @Value("${token.expires}")
    private Long exitTime;
    /**
     * 加密token.
     */
    public String getToken(String userId, String userName, String userRole) {
        //这个是放到负载payLoad 里面,值可以使用常量类进行封装.
        String token = JWT
                .create()
                .withClaim("userId" ,userId)
                .withClaim("userName" ,userName)
                .withClaim("userRole", userRole)
                .withClaim("timeStamp", System.currentTimeMillis())
                // 设置过期时间
                .withExpiresAt(new Date(System.currentTimeMillis()+exitTime))
                .sign(Algorithm.HMAC256(Base64.getEncoder().encode(secretKey.getBytes())));
        return token;
    }
    /**
     * 解析token.
     * {
     * "userId": "weizhong",
     * "userRole": "ROLE_ADMIN",
     * "timeStamp": "134143214"
     * }
     */
    public Map<String, String> parseToken(String token) {
        HashMap<String, String> map = new HashMap<String, String>();
        DecodedJWT decodedjwt = JWT.require(Algorithm.HMAC256(Base64.getEncoder().encode(secretKey.getBytes())))
                .build().verify(token);
        Claim userId = decodedjwt.getClaim("userId");
        Claim userName = decodedjwt.getClaim("userName");
        Claim userRole = decodedjwt.getClaim("userRole");
        Claim timeStamp = decodedjwt.getClaim("timeStamp");
        map.put("userId", userId.asString());
        map.put("userName", userName.asString());
        map.put("userRole", userRole.asString());
        map.put("timeStamp", timeStamp.asLong().toString());
        return map;
    }
}
