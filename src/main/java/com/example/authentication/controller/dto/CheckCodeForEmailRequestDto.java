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
public class CheckCodeForEmailRequestDto {

    @NotBlank(message = "code should not be blank")
    @Size(min = 6, max = 6, message = "code's length is 6")
    private final String code;

    @Email(message = "should be in email format!")
    @NotBlank(message = "email should not be blank!")
    private final String email;

    @NotBlank(message = "token should not be blank")
    private final String token;
}
