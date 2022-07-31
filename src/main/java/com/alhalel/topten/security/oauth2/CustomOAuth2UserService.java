package com.alhalel.topten.security.oauth2;

import com.alhalel.topten.exceptions.OAuth2AuthenticationProcessingException;
import com.alhalel.topten.security.UserPrincipal;
import com.alhalel.topten.security.oauth2.user.OAuth2UserInfo;
import com.alhalel.topten.security.oauth2.user.OAuth2UserInfoFactory;
import com.alhalel.topten.user.AuthProvider;
import com.alhalel.topten.user.Role;
import com.alhalel.topten.user.User;
import com.alhalel.topten.user.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Set;

@AllArgsConstructor
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                oAuth2User.getAttributes()
        );
        if (StringUtils.isBlank(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).map(dbUser -> {
            if (!dbUser.getProvider().equals(AuthProvider.valueOf(registrationId))) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with "
                                + dbUser.getProvider() + " account. Please use your "
                                + dbUser.getProvider() + " account to login.");
            }
            return updateExistingUser(dbUser, oAuth2UserInfo);

        }).orElseGet(() -> registerNewUser(oAuth2UserRequest, oAuth2UserInfo));


        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRoles(Set.of(Role.ROLE_USER));
        return userRepository.save(user);
    }

    private User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRoles(Set.of(Role.ROLE_USER));
        return userRepository.save(user);
    }
}
