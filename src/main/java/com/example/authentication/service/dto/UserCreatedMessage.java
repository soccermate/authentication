package com.example.authentication.service.dto;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.entity.appUser.Provider;
import com.example.authentication.entity.appUser.Role;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
@EqualsAndHashCode
public class UserCreatedMessage {

    private Long id;

    private final String email;

    private final Provider provider;

    private final String externalProviderName;

    private final Role role;

    public UserCreatedMessage(AppUser appUser)
    {
        this.id = appUser.getId();
        this.email = appUser.getEmail();
        this.provider = appUser.getProvider();
        this.externalProviderName = appUser.getExternalProviderName();
        this.role = appUser.getRole();
    }
}
