package com.example.authentication.service;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.entity.appUser.Provider;
import com.example.authentication.entity.appUser.Role;
import com.example.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final MessageService messageService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveExternalUser(AppUser appUser)
    {
        userRepository.saveExternalProviderUser(appUser.getRole().toString(), appUser.getProvider().toString(), appUser.getExternalProviderName());
    }

    public Optional<AppUser> findByExternalProviderNameAndProvider(String externalProviderName, Provider provider)
    {
        return userRepository.findByExternalProviderNameAndProvider(externalProviderName, provider);
    }

    public Optional<AppUser> findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public Optional<AppUser> findById(long id){
        return userRepository.findById(id);
    }

    public AppUser saveUser(String email, String raw_password)
    {
        AppUser user = AppUser.builder()
                .email(email)
                .password(passwordEncoder.encode(raw_password))
                .build();

        AppUser created = userRepository.save(user);

        messageService.sendMessage(created).whenComplete((sendResult, err) ->{
            log.debug("current thread: " + Thread.currentThread().toString());
            if(err != null)
            {
                log.debug("user created successful with message: ");
                log.debug(sendResult.toString());
            }
            else{
                log.error(err.toString());
            }
        });


        return created;
    }

    public boolean isDuplicateEmail(String email)
    {
        return userRepository.existByEmail(email);
    }

    public void changePassword(String email, String newPassword)
    {
        userRepository.changePassword(email, passwordEncoder.encode(newPassword));
    }

    public void changeUserRoleToUser(Long id)
    {
        userRepository.changeUserRole(id, Role.USER);
    }
}
