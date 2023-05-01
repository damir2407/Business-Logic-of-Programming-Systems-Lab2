package com.example.blps_lab1.controller;

import com.example.blps_lab1.dto.UserDTOMapper;
import com.example.blps_lab1.dto.request.RefreshTokenRequest;
import com.example.blps_lab1.dto.request.SignInRequest;
import com.example.blps_lab1.dto.request.SignUpRequest;
import com.example.blps_lab1.dto.response.NewTokenResponse;
import com.example.blps_lab1.dto.response.UserResponse;
import com.example.blps_lab1.model.Jwt;
import com.example.blps_lab1.model.basic.ERole;
import com.example.blps_lab1.model.basic.User;
import com.example.blps_lab1.service.RefreshTokenService;
import com.example.blps_lab1.service.UserService;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserDTOMapper userDTOMapper;
    private final SessionRegistry sessionRegistry;

    public List<SessionInformation> getAllSessions() {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<SessionInformation> activeSessions = new ArrayList<>();
        for (Object principal : principals) {
            List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
            activeSessions.addAll(sessionsInfo);
        }
        return activeSessions;
    }
    public UserController(UserService userService, RefreshTokenService refreshTokenService, UserDTOMapper userDTOMapper, SessionRegistry sessionRegistry) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.userDTOMapper = userDTOMapper;
        this.sessionRegistry = sessionRegistry;

    }

    @PostMapping("login")
    public NewTokenResponse authUser(@Valid @RequestBody SignInRequest signInRequest) {
        Jwt jwt = userService.authUser(signInRequest);
        Jwt refreshJWT = refreshTokenService.createRefreshToken(signInRequest.getLogin());
        System.out.println(getAllSessions());
        return new NewTokenResponse(jwt.getToken(), refreshJWT.getToken());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userService.saveNewUser(signUpRequest, ERole.ROLE_USER);
        return userDTOMapper.apply(user);
    }


    @PostMapping("refreshToken")
    @ResponseStatus(HttpStatus.CREATED)
    public NewTokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.createNewToken(refreshTokenRequest);
    }

    @PostMapping("admin-create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        User admin = userService.saveNewUser(signUpRequest, ERole.ROLE_ADMIN);
        return userDTOMapper.apply(admin);
    }

    @PostMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
    }



}
