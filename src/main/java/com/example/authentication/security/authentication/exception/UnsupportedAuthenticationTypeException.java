package com.example.authentication.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class UnsupportedAuthenticationTypeException extends AuthenticationException {
    public UnsupportedAuthenticationTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnsupportedAuthenticationTypeException(String msg)
    {
        super(msg);
    }
}
