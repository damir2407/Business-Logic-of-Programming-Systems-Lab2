package com.example.blps_lab1.controller;

import com.example.blps_lab1.dto.request.LogOutRequest;
import com.example.blps_lab1.dto.request.RefreshTokenRequest;
import com.example.blps_lab1.dto.request.SignInRequest;
import com.example.blps_lab1.dto.request.SignUpRequest;
import com.example.blps_lab1.dto.response.NewTokenResponse;
import com.example.blps_lab1.dto.response.SuccessResponse;
import com.example.blps_lab1.model.Jwt;
import com.example.blps_lab1.model.basic.ERole;
import com.example.blps_lab1.model.basic.RefreshToken;
import com.example.blps_lab1.service.RefreshTokenService;
import com.example.blps_lab1.service.UserService;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("login")
    public ResponseEntity<?> authUser(@Valid @RequestBody SignInRequest signInRequest) {
        Jwt jwt = userService.authUser(signInRequest);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(signInRequest.getLogin());
        return new ResponseEntity<>(new NewTokenResponse(jwt.getToken(), refreshToken.getToken()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.saveNewUser(signUpRequest, ERole.ROLE_USER);
        return new ResponseEntity<>(new SuccessResponse("Пользователь успешно зарегистрирован!"), HttpStatus.CREATED);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logOutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserLogin(logOutRequest);
        return new ResponseEntity<>(new SuccessResponse("Вы вышли из аккаунта"), HttpStatus.OK);
    }

    @PostMapping("refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        NewTokenResponse newTokenResponse = refreshTokenService.createNewToken(refreshTokenRequest);
        return new ResponseEntity<>(newTokenResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("admin-create")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.saveNewUser(signUpRequest, ERole.ROLE_ADMIN);
        return new ResponseEntity<>(new SuccessResponse("Администратор успешно зарегистрирован!"), HttpStatus.CREATED);
    }


}
