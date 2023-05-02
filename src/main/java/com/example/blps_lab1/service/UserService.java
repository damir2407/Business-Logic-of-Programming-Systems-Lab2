package com.example.blps_lab1.service;

import com.example.blps_lab1.model.AccessAndRefreshToken;
import com.example.blps_lab1.security.CookUserDetails;
import com.example.blps_lab1.security.JwtUtils;
import com.example.blps_lab1.dto.request.SignInRequest;
import com.example.blps_lab1.dto.request.SignUpRequest;
import com.example.blps_lab1.exception.ResourceAlreadyExistException;
import com.example.blps_lab1.exception.ResourceNotFoundException;
import com.example.blps_lab1.model.basic.ERole;
import com.example.blps_lab1.model.Jwt;
import com.example.blps_lab1.model.basic.Role;
import com.example.blps_lab1.model.basic.User;
import com.example.blps_lab1.repository.basic.RoleRepository;
import com.example.blps_lab1.repository.basic.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public AccessAndRefreshToken authUser(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        signInRequest.getLogin(),
                        signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CookUserDetails userDetails = (CookUserDetails) authentication.getDetails();
        String accessToken = jwtUtils.generateJWTToken(userDetails.getUsername(), (Set<Role>) userDetails.getAuthorities());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername(), (Set<Role>) userDetails.getAuthorities());
        return new AccessAndRefreshToken(accessToken, refreshToken);
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + login + " не существует"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь с почтой " + email + " не существует"));
    }


    public boolean checkLoginOnExist(String login) {
        return userRepository.existsUserByLogin(login);
    }

    public boolean checkEmailOnExist(String email) {
        return userRepository.existsUserByEmail(email);
    }


    public User saveNewUser(SignUpRequest signUpRequest, ERole role) {
        if (checkLoginOnExist(signUpRequest.getLogin()))
            throw new ResourceAlreadyExistException("Этот логин уже занят! Попробуйте другой");

        if (checkEmailOnExist(signUpRequest.getEmail()))
            throw new ResourceAlreadyExistException("Эта почта уже занята! Попробуйте другую");

        Set<Role> roles = new HashSet<>();
        switch (role) {
            case ROLE_USER -> {
                Role userRole = roleRepository
                        .findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new ResourceNotFoundException("Роль USER не найдена!"));
                roles.add(userRole);
            }
            case ROLE_ADMIN -> {
                Role adminRole = roleRepository
                        .findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("Роль ADMIN не найдена!"));
                roles.add(adminRole);
            }
            default -> throw new ResourceNotFoundException("Роль не найдена!");
        }

        User user = new User(signUpRequest.getLogin(), passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(), roles);

        userRepository.save(user);
        return user;
    }

    public void incrementCulinaryNewsCounter(String user_login) {
        User user = findUserByLogin(user_login);
        user.setCulinaryNewsCount(user.getCulinaryNewsCount() + 1);
        userRepository.save(user);
    }

}
