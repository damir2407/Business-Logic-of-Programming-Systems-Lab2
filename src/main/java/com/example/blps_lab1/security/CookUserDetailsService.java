package com.example.blps_lab1.security;

import com.example.blps_lab1.model.basic.User;
import com.example.blps_lab1.repository.basic.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CookUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsernameAndRoles(String login, Collection<? extends GrantedAuthority> roles) throws UsernameNotFoundException {
        return CookUserDetails.build(login, roles);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.
                findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователя с логином " + login + " не существует"));
        return CookUserDetails.build(user);
    }
}
