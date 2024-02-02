package damon.backend.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component

public class SocialClientRegistration {

    public ClientRegistration naverClientRegistration() {

        return ClientRegistration.withRegistrationId("naver")
                .clientId("spdOdDxRE5b1jvsH5qAi")
                .clientSecret("KP0ktDajj2")
                .redirectUri("http://localhost:3000/oauth2/redirect")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("name", "email", "profile_image")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .build();
    }

    public ClientRegistration kakaoClientRegistration() {

        return ClientRegistration.withRegistrationId("kakao")
                .clientId("4cf142eea5e1f3b95bb9b3d4d44f27f4")
                .clientSecret("tsGJuRvdmLv9HXyVT4z7zFIs8VDCAEW4q")
                .redirectUri("http://localhost:3000/oauth2/redirect")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile_nickname", "profile_image", "account_email")
                .authorizationUri("https://kauth.kakao.com/oauth2.0/authorize")
                .tokenUri("https://kauth.kakao.com/oauth2.0/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .build();
    }
}

