package com.example.authentication.security.config;

import com.example.authentication.controller.config.PathProperties;
import com.example.authentication.security.authentication.filters.AuthenticationFilter;
import com.example.authentication.security.oauth2.CustomAuthorizedClientService;
import com.example.authentication.security.oauth2.CustomStatelessAuthorizationRequestRepository;
import com.example.authentication.security.oauth2.SuccessAndFailureHandlers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.validation.Validator;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //for oauth2
    private final CustomStatelessAuthorizationRequestRepository customStatelessAuthorizationRequestRepository;

    private final CustomAuthorizedClientService customAuthorizedClientService;

    private final SuccessAndFailureHandlers successAndFailureHandlers;


    //for authentication
    private final AuthenticationManager authenticationManager;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final Validator validator;


    // the path /oauth2/authorization/google is for google sign in

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {

        http.formLogin().disable();
        http.httpBasic().disable();
        http.csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        setAuthorizationRules(http);

        oauth2Configure(http);

        addAuthenticationFilter(http);



        return http.build();
    }

    private void setAuthorizationRules(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                .anyRequest()
                .permitAll();
    }

    private void oauth2Configure(HttpSecurity http) throws Exception
    {
        http.oauth2Login(config -> {

            config.authorizationEndpoint(subconfig -> {
                subconfig.authorizationRequestRepository(this.customStatelessAuthorizationRequestRepository);
            });

            config.authorizedClientService(customAuthorizedClientService);

            config.successHandler(successAndFailureHandlers::oauthSuccessResponse);
            config.failureHandler(successAndFailureHandlers::oauthFailureResponse);
        });
    }

    private void addAuthenticationFilter(HttpSecurity http) throws Exception
    {
        //set authentication filter
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(
                        new MvcRequestMatcher(new HandlerMappingIntrospector(),
                                PathProperties.LOGIN),
                        authenticationManager,
                        validator);

        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
