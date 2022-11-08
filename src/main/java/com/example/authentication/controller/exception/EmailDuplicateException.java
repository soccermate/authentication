package com.example.authentication.controller.exception;

public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException(String msg)
    {
        super(msg);
    }
}
