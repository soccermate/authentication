package com.example.authentication.security.authentication.handlers;

import com.example.authentication.security.authentication.userDetails.AppUserDetail;
import com.example.authentication.security.jwt.TokenService;
import com.example.authentication.security.oauth2.dto.TokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@AllArgsConstructor
public class OnSuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.debug("onAuthenticationSuccess() called!");
        log.debug("authentication: " + authentication.toString());

        AppUserDetail appUserDetails = (AppUserDetail) authentication.getDetails();

        log.debug("appUserDetails: " + appUserDetails.toString());

        String access_token =  tokenService.createAuthenticationAccessToken(appUserDetails.getUser_id(), appUserDetails.getRole());
        String refresh_token = tokenService.createAuthenticationRefreshToken(appUserDetails.getUser_id(), appUserDetails.getRole());

        TokenDto tokenDto = new TokenDto(access_token, refresh_token, appUserDetails.getUser_id());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        objectMapper.writeValue(response.getWriter(), tokenDto);

        response.flushBuffer();
    }
}
