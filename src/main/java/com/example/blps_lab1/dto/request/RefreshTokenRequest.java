package com.example.blps_lab1.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;

}
