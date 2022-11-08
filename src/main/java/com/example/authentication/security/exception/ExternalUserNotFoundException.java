package com.example.authentication.security.exception;


public class ExternalUserNotFoundException extends RuntimeException
{
    public ExternalUserNotFoundException(String msg)
    {
        super(msg);
    }
}
