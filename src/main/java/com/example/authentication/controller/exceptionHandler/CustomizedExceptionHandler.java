package com.example.authentication.controller.exceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.authentication.controller.config.PathProperties;
import com.example.authentication.controller.exception.EmailDuplicateException;
import com.example.authentication.dto.ErrorMsgDto;
import com.example.authentication.security.jwt.exception.JwtMalformedException;
import com.example.authentication.security.jwt.exception.JwtNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
@Slf4j
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JwtNotFoundException.class)
    public final ResponseEntity<Object> handleJwtNotFoundException(Exception ex, WebRequest request)
    {
        log.debug("jwt token not found!");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "jwt token is not found!",
                ex.getMessage()
        );


        return new ResponseEntity(exceptionResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public final ResponseEntity<Object> handleJwtVerificationException(Exception ex, WebRequest request)
    {
        log.debug("token verification failed");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "token cannot be verified",
                ex.getMessage()
        );


        return new ResponseEntity(exceptionResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtMalformedException.class)
    public final ResponseEntity<Object> handleJwtMalformedException(Exception ex, WebRequest request)
    {
        log.debug("token is malformed");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "the format of the token content is not right",
                ex.getMessage()
        );


        return new ResponseEntity(exceptionResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public final ResponseEntity<Object> handleEmailDuplicateException(Exception ex, WebRequest request)
    {
        log.debug("EmailDuplicateExceptionException thrown");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "duplicate email",
                ex.getMessage()
        );


        return new ResponseEntity(exceptionResult, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleDataIntegrityViolationExceptionException(Exception ex, WebRequest request)
    {
        log.debug("DataIntegrityViolationExceptionException thrown");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "cannot update or add data in db",
                ex.getMessage()
        );



        return new ResponseEntity(exceptionResult, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleUsernameNotFoundException(Exception ex, WebRequest request)
    {
        log.debug("usernameNotFoundException thrown");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "the user cannot be found in the system",
                ex.getMessage()
        );


        if(((ServletWebRequest)request).getRequest().getRequestURI().equals(PathProperties.PASSWORD_EMAIL_SEND)) {
            return new ResponseEntity(exceptionResult, HttpStatus.CONFLICT);
        }


        return new ResponseEntity(exceptionResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleExceptionException(Exception ex, WebRequest request)
    {
        log.debug("an exception is thrown");

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "an exception is thrown",
                ex.getMessage()
        );


        return new ResponseEntity(exceptionResult, HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex, HttpHeaders headers,
             HttpStatus status, WebRequest request)
    {
        log.debug("handleMethodArgumentNotValid is called");

        String rejectedValue = ex.getBindingResult().getFieldError().getRejectedValue() == null? ""
                : ex.getBindingResult().getFieldError().getRejectedValue().toString();
        String message = ex.getBindingResult().getFieldError().getDefaultMessage() == null? ""
                :ex.getBindingResult().getFieldError().getDefaultMessage();

        ErrorMsgDto exceptionResult = new ErrorMsgDto(
                new Date(),
                "not a valid format",
                rejectedValue
                        + " is not valid with message : "
                        + message
        );

        return new ResponseEntity(exceptionResult, HttpStatus.BAD_REQUEST);
    }
}
