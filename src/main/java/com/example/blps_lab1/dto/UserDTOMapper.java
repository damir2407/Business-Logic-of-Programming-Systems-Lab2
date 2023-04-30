package com.example.blps_lab1.dto;

import com.example.blps_lab1.dto.response.UserResponse;
import com.example.blps_lab1.model.basic.User;

import java.util.function.Function;

public class UserDTOMapper implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        return new UserResponse(
                user.getLogin(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
