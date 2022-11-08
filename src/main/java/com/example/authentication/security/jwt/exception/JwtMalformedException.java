package com.example.authentication.security.jwt.exception;

public class JwtMalformedException extends RuntimeException{
    public JwtMalformedException(String msg)
    {
        super(msg);
    }
}
