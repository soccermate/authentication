package com.example.authentication.security.authentication.providers;

import com.example.authentication.security.authentication.exception.AuthenticationMissingInfoException;
import com.example.authentication.security.authentication.exception.PasswordNotMatchingException;
import com.example.authentication.security.authentication.exception.UnsupportedAuthenticationTypeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!supports(authentication.getClass()))
        {
            throw new UnsupportedAuthenticationTypeException("authentication type: " + authentication.getClass()+ " not supported");
        }

        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        if(username == null || rawPassword == null)
        {
            throw new AuthenticationMissingInfoException("username or password is null");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

        if(passwordEncoder.matches(rawPassword, userDetails.getPassword()))
        {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            auth.setDetails(userDetails);
            return auth;
        }

        throw new PasswordNotMatchingException("username: " + username + ", password: " + rawPassword + " not matching!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
