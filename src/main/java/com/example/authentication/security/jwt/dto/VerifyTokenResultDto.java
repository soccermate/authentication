package com.example.authentication.security.jwt.dto;

import com.example.authentication.entity.appUser.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VerifyTokenResultDto {

    private final long user_id;

    private final Role role;

    public static VerifyTokenResultDto getInvalidInstance()
    {
        return new VerifyTokenResultDto(-1 , null);
    }

    public boolean isValid()
    {
        return (user_id != -1 ) && (role != null);
    }
}
