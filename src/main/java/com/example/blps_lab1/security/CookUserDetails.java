package com.example.blps_lab1.security;

import com.example.blps_lab1.model.basic.Role;
import com.example.blps_lab1.model.basic.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CookUserDetails implements UserDetails {

    private String login;

    private String password;

    private String email;


    private Collection<? extends GrantedAuthority> authorities;

    public CookUserDetails(String login,  Collection<? extends GrantedAuthority> authorities) {
        this.login = login;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    public static CookUserDetails build(String login, Collection<? extends GrantedAuthority> roles) {

        return new CookUserDetails(login, roles);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getEmail() {
        return email;
    }


}
