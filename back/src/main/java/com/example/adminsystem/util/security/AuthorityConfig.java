package com.example.adminsystem.util.security;

import com.example.adminsystem.entity.NowAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorityConfig {
    @Bean
    public NowAuthority nowAuthority() {
        return new NowAuthority();
    }
}
