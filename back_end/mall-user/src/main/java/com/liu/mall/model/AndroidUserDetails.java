package com.liu.mall.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class AndroidUserDetails implements UserDetails {
    private AndroidUser androidUser;

    public AndroidUserDetails(AndroidUser androidUser) {
        this.androidUser = androidUser;
    }


    /**
     * 主要用于权限验证
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("TEST"));
    }

    @Override
    public String getPassword() {
        return androidUser.getPassword();
    }

    @Override
    public String getUsername() {
        return androidUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public AndroidUser getUserPersonal(){
        return androidUser;
    }
}
