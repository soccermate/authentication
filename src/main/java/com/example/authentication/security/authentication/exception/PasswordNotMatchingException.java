package com.example.authentication.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class PasswordNotMatchingException extends AuthenticationException
{
    public PasswordNotMatchingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PasswordNotMatchingException(String msg) {
        super(msg);
    }
}
