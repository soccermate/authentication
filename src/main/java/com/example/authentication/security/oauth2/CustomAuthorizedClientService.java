package com.example.authentication.security.oauth2;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.entity.appUser.Provider;
import com.example.authentication.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAuthorizedClientService implements OAuth2AuthorizedClientService
{
    private final UserService userService;

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        log.debug("saveAuthorizedClient called!");

        log.debug("Authentication: " + principal.toString());
        log.debug("principalName: " + authorizedClient.getPrincipalName());
        log.debug("principal.getName(): " + principal.getName());
        log.debug("principal.getCredentials()" + String.valueOf(principal.getCredentials()));
        log.debug("principal.getDetails()" + String.valueOf(principal.getDetails()));
        log.debug("authorizedClient.getClientRegistration: " + authorizedClient.getClientRegistration());

        String principalName = principal.getName();
        String provider = authorizedClient.getClientRegistration().getRegistrationId().toUpperCase();
        AppUser appUser = AppUser.builder()
                .provider(Enum.valueOf(Provider.class, provider))
                .externalProviderName(principalName).build();

        try{
            userService.saveExternalUser(appUser);
        }
        catch (DataIntegrityViolationException integrityException)
        {
            log.debug("existing user");
            log.debug(integrityException.toString());
        }
        catch (Exception exception)
        {
            log.error("error with " + principal.toString());
            log.error(exception.toString());
        }



    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {

    }
}
