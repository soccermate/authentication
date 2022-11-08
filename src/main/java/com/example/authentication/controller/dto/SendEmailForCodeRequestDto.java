package com.example.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SendEmailForCodeRequestDto {

    @Email(message = "should be in email format!")
    @NotBlank(message = "email should not be blank!")
    private final String email;


}
