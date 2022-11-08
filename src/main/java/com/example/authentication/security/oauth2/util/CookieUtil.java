package com.example.authentication.security.oauth2.util;

import lombok.NonNull;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;

import javax.servlet.http.Cookie;
import java.util.Optional;

import static java.util.Objects.isNull;

public class CookieUtil
{
    private static final String COOKIE_NAME = "SESSION_ID";

    private static final String COOKIE_DOMAIN = "localhost";
    private static final Boolean HTTP_ONLY = Boolean.TRUE;
    private static final Boolean SECURE = Boolean.FALSE;

    public static Optional<String> retrieve(Cookie[] cookies) {
        if (isNull(cookies)) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(COOKIE_NAME)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    public static String generateCookie(@NonNull String value) {
        // Build cookie instance
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        if (!"localhost".equals(COOKIE_DOMAIN)) { // https://stackoverflow.com/a/1188145
            cookie.setDomain(COOKIE_DOMAIN);
        }
        cookie.setHttpOnly(HTTP_ONLY);
        cookie.setSecure(SECURE);
        cookie.setMaxAge((int) 5000);
        cookie.setPath("/");
        // Generate cookie string
        Rfc6265CookieProcessor processor = new Rfc6265CookieProcessor();
        return processor.generateHeader(cookie);
    }

}
