package com.example.authentication.security.authentication.handlers;

import com.example.authentication.dto.ErrorMsgDto;
import com.example.authentication.security.authentication.exception.PasswordNotMatchingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class OnFailureAuthenticationHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.debug("onAuthenticationFailure called!");

        if(exception instanceof PasswordNotMatchingException){
            setErrorResponse(HttpStatus.BAD_REQUEST, response,  "비밀번호가 맞지 않습니다." ,exception.getMessage());
        } else if(exception instanceof UsernameNotFoundException){
            setErrorResponse(HttpStatus.NOT_FOUND, response, "존재하지 않는 아이디 입니다.", exception.getMessage() );
        } else if(exception instanceof AuthenticationException){
            setErrorResponse(HttpStatus.BAD_REQUEST, response, "인증 에러", exception.getMessage());
        } else if(exception instanceof AuthenticationServiceException){
            setErrorResponse(HttpStatus.BAD_REQUEST, response, "부적절한 요청입니다.", exception.getMessage());
        } else {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, "서버에 문제가 있습니다.", exception.getMessage());
        }

    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, String errorMessage, String details) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        objectMapper.writeValue(response.getWriter(), new ErrorMsgDto(new Date(), errorMessage, details));
    }
}
