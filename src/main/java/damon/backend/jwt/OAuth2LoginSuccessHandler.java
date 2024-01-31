package damon.backend.jwt;

import damon.backend.dto.login.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    // 로그인 성공 시 Jwt 토큰 발급
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String providername = customOAuth2User.getProvidername();

        // 로그인 성공 로그 기록
        log.info("로그인이 성공했습니다. 사용자: " + customOAuth2User.getProvidername());

        // jwt 토큰 발급
        String token = jwtUtil.createJwt(providername, 3600000L);

//      // 리프레시 토큰 발급
//      String refreshToken = jwtUtil.createRefreshToken(providerName, 28 * 24 * 3600000L); // 28 days

        log.info("JWT 토큰 : " + token);

        // 프론트엔드로 리다이렉트하면서 JWT 토큰을 URL 파라미터로 전달
        String redirectUrl = "http://localhost:3000/oauth2/redirect?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
//      response.addHeader("Refresh-Token", "Bearer " + refreshToken);




}

