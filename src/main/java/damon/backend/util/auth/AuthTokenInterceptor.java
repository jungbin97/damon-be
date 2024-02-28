package damon.backend.util.auth;

import damon.backend.dto.response.user.TokenDto;
import damon.backend.exception.custom.AccessTokenNotFoundException;
import damon.backend.exception.custom.RefreshTokenExpiredException;
import damon.backend.exception.custom.RefreshTokenNotFoundException;
import damon.backend.exception.custom.TokenNotValidatedException;
import damon.backend.util.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            // 컨트롤러 메서드가 아닌 경우 요청 허용
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthToken authToken = handlerMethod.getMethodAnnotation(AuthToken.class);

        if (authToken != null) {
            // @AuthToken 어노테이션이 붙은 핸들러 메서드인 경우

            // 엑세스 토큰이 없는 경우 처리
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                throw new AccessTokenNotFoundException(); // -> 프론트에서 엑세스 토큰과 리프레시 토큰을 버려야 함.
            }

            // 엑세스 토큰이 유효하지 않은 경우 처리
            if (Jwt.isNotValidToken(token)) {
                throw new TokenNotValidatedException(); // -> 프론트에서 엑세스 토큰과 리프레시 토큰을 버려야 함.
            }

            // 엑세스 토큰이 만료된 경우 처리
            if (Jwt.isExpiredToken(token)) {
                // 리프레시 토큰이 없는 경우 처리
                String refreshToken = request.getHeader("Refresh-Token");
                if (refreshToken == null || refreshToken.isEmpty()) {
                    throw new RefreshTokenNotFoundException(); // -> 프론트에서 리프레시 토큰을 담아서 보내야 함.
                }

                // 리프레시 토큰이 유효하지 않은 경우 처리
                if (Jwt.isNotValidToken(refreshToken)) {
                    throw  new TokenNotValidatedException(); // -> 프론트에서 엑세스 토큰과 리프레시 토큰을 버려야 함.
                }

                if (Jwt.isExpiredToken(refreshToken)) {
                    throw  new RefreshTokenExpiredException(); // -> 프론트에서 엑세스 토큰과 리프레시 토큰을 버려야 함.
                }

                // 리프래시 토큰이 유효한 경우, 새로운 토큰 생성
                String newAccessToken = generateNewAccessToken(refreshToken);
                String newRefreshToken = generateNewRefreshToken(refreshToken);

                // 새로 생성한 토큰을 응답 헤더에 설정
                response.setHeader("New-Access-Token", newAccessToken);
                response.setHeader("New-Refresh-Token", newRefreshToken);

                // 요청 처리 허용 -> 프론트에서 엑세스 토큰과 리프레시 토큰을 갱신해야 함.
                return true;
            }
        }

        // 토큰이 필요하지 않거나 유효한 경우
        return true;
    }

    private String generateNewAccessToken(String refreshToken) {
        // 리프래시 토큰에서 사용자 정보 추출
        TokenDto tokenDto = Jwt.getUserDtoByToken(refreshToken);

        // 새로운 엑세스 토큰 생성
        String newAccessToken = Jwt.generateAccessToken(tokenDto.getIdentifier());

        return newAccessToken;
    }

    private String generateNewRefreshToken(String refreshToken) {
        // 리프래시 토큰에서 사용자 정보 추출
        TokenDto tokenDto = Jwt.getUserDtoByToken(refreshToken);

        // 새로운 리프래시 토큰 생성
        String newRefreshToken = Jwt.generateRefreshToken(tokenDto.getIdentifier());

        return newRefreshToken;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) throws Exception {}

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) throws Exception {}

}