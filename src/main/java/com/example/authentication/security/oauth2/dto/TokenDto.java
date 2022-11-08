package com.example.authentication.security.oauth2.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(force = true)
@Data
@RequiredArgsConstructor
public class TokenDto {

    private final String access_token;

    private final String refresh_token;

    private final Long user_id;

}
