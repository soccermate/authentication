package com.example.authentication.security.authentication.filters;


import com.example.authentication.security.authentication.dto.LoginRequestDto;
import com.example.authentication.security.authentication.exception.UnsupportedAuthenticationTypeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Order(2)
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter
{

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Validator validator;

    public AuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                                AuthenticationManager authenticationManager, Validator validator) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.validator = validator;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException
    {
        log.debug(request.toString() + " is in login filter!");

        if(!HttpMethod.POST.name().equals(request.getMethod())
                || !request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            throw new UnsupportedAuthenticationTypeException("request with " + request.getMethod()
                    + " " + request.getContentType() + " cannot be processed in login filter" );
        }

        LoginRequestDto loginRequestDto;

        try{
            loginRequestDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        } catch(IOException e){
            throw new AuthenticationServiceException("failed to transform to LoginRequestDto.class");
        }

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(loginRequestDto);
        if(!violations.isEmpty())
        {
            throw new AuthenticationServiceException("failed to validate loginRequestDto: " + loginRequestDto.toString());
        }

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }




}
