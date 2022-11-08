package com.example.authentication.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ChangePasswordRequestDto {

    @Email(message = "should be in email format!")
    @NotBlank(message = "email should not be blank!")
    private final String email;

    @NotBlank(message = "token should not be blank")
    private final String token;

    @NotBlank(message = "password should not be blank")
    @Size(min = 8, max = 20, message = "password's length is between 8~20")
    private final String password;
}
