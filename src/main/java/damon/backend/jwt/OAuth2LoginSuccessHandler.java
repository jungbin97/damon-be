package damon.backend.jwt;

import damon.backend.dto.login.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    // 로그인 성공 시 토큰 발급 (여기서 JWT랑 헤더 생성하면 됨)

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String providername = customOAuth2User.getProvidername();

        String token = jwtUtil.createJwt(providername, 3600000L);

        response.addHeader("Authorization", "Bearer " + token);

    }
}

