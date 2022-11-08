package com.example.authentication.security.jwt.exception;

public class JwtNotFoundException extends RuntimeException{
    public JwtNotFoundException(String msg)
    {
        super(msg);
    }
}
