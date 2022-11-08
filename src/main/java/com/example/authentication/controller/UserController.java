package com.example.authentication.controller;

import com.example.authentication.controller.config.PathProperties;
import com.example.authentication.controller.dto.*;
import com.example.authentication.controller.exception.EmailDuplicateException;
import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.security.jwt.TokenService;
import com.example.authentication.security.jwt.dto.VerifyTokenResultDto;
import com.example.authentication.security.oauth2.dto.TokenDto;
import com.example.authentication.service.UserService;
import com.example.authentication.util.MailSender;
import com.example.authentication.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController
{
    private final TokenService tokenService;

    private final UserService userService;

    private final MailSender mailSender;

    @PutMapping("/{id}/role")
    public void changeRoleToUser(@PathVariable long id)
    {
        userService.changeUserRoleToUser(id);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto)
    {

        log.debug("refresh token called!");
        log.debug("request body:");
        log.debug(refreshTokenRequestDto.toString());
        //verify token and get id and role for the token

        VerifyTokenResultDto verifyTokenResultDto = tokenService.verifyToken(refreshTokenRequestDto.getRefresh_token());

        AppUser appUser = userService.findById(verifyTokenResultDto.getUser_id()).orElseThrow(
                () -> new UsernameNotFoundException("the user_id in the token cannot be found in our system!")
        );

        String access_token = tokenService
                .createAuthenticationAccessToken(appUser.getId(), appUser.getRole());

        String refresh_token = tokenService
                .createAuthenticationRefreshToken(appUser.getId(), appUser.getRole());

        return ResponseEntity.ok(new TokenDto(access_token, refresh_token, appUser.getId()));
    }

    @PostMapping("/signup/emailsend")
    public ResponseEntity<SendEmailForCodeResponseDto> sendEmailForSignUp(
            @Valid @RequestBody SendEmailForCodeRequestDto sendEmailForCodeRequestDto)
    {

        String email = sendEmailForCodeRequestDto.getEmail();
        String code = RandomCodeGenerator.generateRandomCode(6);

        if(userService.isDuplicateEmail(email))
        {
            log.debug("email duplicate!");
            throw new EmailDuplicateException(email + " already exist!");
        }


        mailSender.sendMailForSignup(email, code)
                .whenComplete((result, ex) ->{
                    if(ex == null && result){
                        log.debug("successfully sent email!");
                    }
                    else if(ex != null)
                    {

                        log.error("error in sending email!");
                        log.error(ex.getMessage());
                    }
                    else{
                        log.error("error not generated but failed. something went wrong");
                    }
                });

        String token = tokenService.createSignUpCreateCodeToken(email, code);

        log.debug("created token: " + token);

        return ResponseEntity.ok(new SendEmailForCodeResponseDto(token ));
    }

    @PostMapping("/signup/emailcodecheck")
    public ResponseEntity<CheckCodeForEmailResponseDto> checkCodeForSignUp(
            @Valid @RequestBody CheckCodeForEmailRequestDto checkCodeForEmailRequestDto)
    {
        String email = checkCodeForEmailRequestDto.getEmail();
        String code = checkCodeForEmailRequestDto.getCode();
        String token = checkCodeForEmailRequestDto.getToken();

        String new_token = tokenService.verifySignUpCodeToken(token, email, code);

        log.debug("new token: " + new_token);

        return ResponseEntity.ok(new CheckCodeForEmailResponseDto(new_token));
    }

    @PostMapping("")
    public ResponseEntity signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto)
    {
        String email = signUpRequestDto.getEmail();
        String token = signUpRequestDto.getToken();
        String password = signUpRequestDto.getPassword();

        tokenService.verifySignUpAfterCodeToken(token, email);

        userService.saveUser(email, password);

        return ResponseEntity.created(null).build();
    }

    @PostMapping("/password/emailsend")
    public ResponseEntity<SendEmailForCodeResponseDto> sendEmailForPassword(
            @Valid @RequestBody SendEmailForCodeRequestDto sendEmailForCodeRequestDto)
    {

        String email = sendEmailForCodeRequestDto.getEmail();
        String code = RandomCodeGenerator.generateRandomCode(6);

        if(!userService.isDuplicateEmail(email))
        {
            log.debug("email not found!");
            throw new UsernameNotFoundException(email + " not found in our system!");
        }

        mailSender.sendMailForPassword(email, code)
                .whenComplete((result, ex) ->{
                    if(ex == null && result){
                        log.debug("successfully sent email!");
                    }
                    else if(ex != null)
                    {
                        log.error("error in sending email!");
                        log.error(ex.getMessage());
                    }
                    else{
                        log.error("error not generated but failed. something went wrong");
                    }
                });

        String token = tokenService.createPasswordCreateCodeToken(email, code);

        log.debug("created token: " + token);

        return ResponseEntity.ok(new SendEmailForCodeResponseDto(token ));
    }

    @PostMapping("/password/emailcodecheck")
    public ResponseEntity<CheckCodeForEmailResponseDto> checkCodeForPassword(
            @Valid @RequestBody CheckCodeForEmailRequestDto checkCodeForEmailRequestDto)
    {
        String email = checkCodeForEmailRequestDto.getEmail();
        String code = checkCodeForEmailRequestDto.getCode();
        String token = checkCodeForEmailRequestDto.getToken();

        String new_token = tokenService.verifyPasswordCodeToken(token, email, code);

        log.debug("new token: " + new_token);

        return ResponseEntity.ok(new CheckCodeForEmailResponseDto(new_token));
    }

    @PutMapping("/password")
    public void ChangePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto)
    {
        String token = changePasswordRequestDto.getToken();
        String email = changePasswordRequestDto.getEmail();
        String password = changePasswordRequestDto.getPassword();

        tokenService.verifyPasswordAfterCodeToken(token, email);

        userService.changePassword(email, password);

    }







}
