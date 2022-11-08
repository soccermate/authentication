package com.example.authentication.security.authentication.userDetails;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.entity.appUser.Provider;
import com.example.authentication.entity.appUser.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@ToString
@Getter
public class AppUserDetail implements UserDetails
{
    private final Long user_id;
    private final Role role;
    private final String email;
    private final String password;
    private final String externalProviderName;
    private final Provider provider;

    public AppUserDetail(AppUser appUser)
    {
        this.user_id = appUser.getId();
        this.role = appUser.getRole();
        this.email = appUser.getEmail();
        this.password = appUser.getPassword();
        this.externalProviderName = appUser.getExternalProviderName();
        this.provider = appUser.getProvider();
    }

    public AppUserDetail(long user_id, Role role)
    {
        this.user_id = user_id;
        this.role = role;
        this.provider = null;
        this.externalProviderName = null;
        this.password = null;
        this.email = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
