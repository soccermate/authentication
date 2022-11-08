package com.example.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RefreshTokenRequestDto {

    @NotBlank
    private final String refresh_token;
}
