package com.example.authentication.security.oauth2;


import com.example.authentication.dto.ErrorMsgDto;
import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.entity.appUser.Provider;
import com.example.authentication.security.exception.ExternalUserNotFoundException;
import com.example.authentication.security.jwt.TokenService;
import com.example.authentication.security.oauth2.dto.TokenDto;
import com.example.authentication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
@AllArgsConstructor
public class SuccessAndFailureHandlers {

    private final TokenService tokenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserService userService;

    @SneakyThrows
    public void oauthSuccessResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        log.debug("oauthSuccessResponse called!");
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;


        String principalName = oAuth2AuthenticationToken.getName();
        Provider provider = Enum.valueOf(Provider.class,
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().toUpperCase());

        log.debug("principalName: " + principalName);
        log.debug("provider: " + provider.toString());

        AppUser appUser = userService.findByExternalProviderNameAndProvider(principalName, provider)
                .orElseThrow(() -> new ExternalUserNotFoundException(oAuth2AuthenticationToken.toString() + " not found in db"));

        response.setStatus(HttpStatus.OK.value());
        log.debug(authentication.toString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String access_token = tokenService.createAuthenticationAccessToken(appUser.getId(), appUser.getRole());
        String refresh_token = tokenService.createAuthenticationRefreshToken(appUser.getId(), appUser.getRole());

        TokenDto tokenDto = new TokenDto(access_token, refresh_token, appUser.getId());
        String jsonStr = objectMapper.writeValueAsString(tokenDto);
        response.getWriter().write(jsonStr);

    }

    @SneakyThrows
    public void oauthFailureResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException){

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.error(authenticationException.toString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorMsgDto errorMsgDto = new ErrorMsgDto(new Date(), "errorFailure", authenticationException.toString());
        String jsonStr = objectMapper.writeValueAsString(errorMsgDto);
        response.getWriter().write(jsonStr);

    }
}
