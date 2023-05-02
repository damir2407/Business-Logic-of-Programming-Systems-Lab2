package com.example.blps_lab1.security;

import com.example.blps_lab1.model.basic.Role;
import com.example.blps_lab1.model.basic.User;
import com.example.blps_lab1.repository.basic.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class CookUserDetailsService implements UserDetailsService {

    public UserDetails loadUserByUsernameAndRoles(String login, Collection<? extends GrantedAuthority> roles) throws UsernameNotFoundException {
        return CookUserDetails.build(login, roles);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
