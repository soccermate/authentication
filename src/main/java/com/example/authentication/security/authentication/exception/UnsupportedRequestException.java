package com.example.authentication.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class UnsupportedRequestException extends AuthenticationException {
    public UnsupportedRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnsupportedRequestException(String msg) {
        super(msg);
    }
}
