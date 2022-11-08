package com.example.authentication.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationMissingInfoException extends AuthenticationException
{

    public AuthenticationMissingInfoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthenticationMissingInfoException(String msg) {
        super(msg);
    }
}
