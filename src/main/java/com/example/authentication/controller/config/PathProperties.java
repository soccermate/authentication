package com.example.authentication.controller.config;

public interface PathProperties
{
    public static final String REFRESH_TOKEN = "/user/token/refresh";
    public static final String LOGIN = "/user/login";
    public static final String SIGN_UP_EMAIL_SEND = "/user/signup/emailsend";
    public static final String SIGN_UP_EMAIL_CHECK_CODE = "/user/signup/emailcodecheck";
    public static final String USER = "/user";
    public static final String PASSWORD_EMAIL_SEND = "/user/password/emailsend";
    public static final String PASSWORD_EMAIL_CHECK_CODE = "/user/password/emailcodecheck";
    public static final String PASSWORD = "/user/password";
    public static final String OAUTH2_GOOGLE_LOGIN = "/oauth2/authorization/google";
}
