package com.example.authentication.security.oauth2;

import com.example.authentication.security.oauth2.util.CookieUtil;
import com.example.authentication.security.util.StringEncAndDecryptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomStatelessAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>
{
    ObjectMapper objectMapper = new ObjectMapper();

    private final StringEncAndDecryptor stringEncAndDecryptor;

    public CustomStatelessAuthorizationRequestRepository(StringEncAndDecryptor stringEncAndDecryptor)
    {
        this.stringEncAndDecryptor = stringEncAndDecryptor;
    }
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        try {
            return getAuthRequestFromRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        String authRequestStr;

        log.debug("saveAuthorizeRequest called!");


        byte[] serialized = SerializationUtils.serialize(authorizationRequest);


        String encryptedRequest = stringEncAndDecryptor.encrypt(serialized);

        log.debug("encrypted request: " + encryptedRequest);

        String cookie = CookieUtil.generateCookie(encryptedRequest);

        log.debug("cookie to set: " + cookie);

        response.setHeader(HttpHeaders.SET_COOKIE, cookie);


    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        try {
            return getAuthRequestFromRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    OAuth2AuthorizationRequest getAuthRequestFromRequest(HttpServletRequest request) throws Exception
    {
        log.debug("getAuthRequestFromRequest called!");
        String encryptedRequest = CookieUtil.retrieve(request.getCookies()).orElseThrow(
                () -> new Exception("cookies! not found!"));

        log.debug("encrypted cookie: " + encryptedRequest);

        byte[] decrypted = stringEncAndDecryptor.decrypt(encryptedRequest);

        log.debug("decrypted cookie: " + decrypted.toString());

        OAuth2AuthorizationRequest authRequest = (OAuth2AuthorizationRequest) SerializationUtils.deserialize(decrypted);

        log.debug("auth request: " + authRequest.toString());

        return authRequest;
    }
}
