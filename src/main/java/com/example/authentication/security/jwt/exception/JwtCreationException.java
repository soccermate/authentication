package com.example.authentication.security.jwt.exception;

import com.auth0.jwt.exceptions.JWTCreationException;

public class JwtCreationException extends RuntimeException {
   public JwtCreationException(String msg)
   {
       super(msg);
   }
}
