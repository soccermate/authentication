package com.example.authentication.security.authentication.userDetailService;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.security.authentication.userDetails.AppUserDetail;
import com.example.authentication.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Check user identifier");
        AppUser user = userService.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("email "+ email + " not found!"));
        return new AppUserDetail(user);
    }
}
