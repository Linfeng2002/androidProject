package com.liu.mall.config;


import com.liu.mall.service.AndroidUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

@Configuration
public class MallSecurityConfig {

    @Resource
    AndroidUserService userPersonalService;
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> userPersonalService.loadUserByUsername(username);
    }
}
