package com.example.authentication.security.authentication.authenticationManager;

import com.example.authentication.security.authentication.providers.EmailPasswordAuthenticationProvider;
import com.example.authentication.security.authentication.exception.UnsupportedAuthenticationTypeException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomAuthenticationManager implements AuthenticationManager
{
    private final EmailPasswordAuthenticationProvider authenticationProvider;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authenticationProvider.supports(authentication.getClass()))
        {
            return authenticationProvider.authenticate(authentication);
        }

        throw new UnsupportedAuthenticationTypeException("authentication type " + authentication.getClass() + "not supported!");
    }
}
