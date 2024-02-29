package damon.backend.util.login;

import damon.backend.exception.custom.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            // 컨트롤러 메서드가 아닌 경우 요청 허용
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthToken authToken = handlerMethod.getMethodAnnotation(AuthToken.class);

        // @AuthToken 어노테이션이 붙은 핸들러 메서드인 경우
        if (authToken != null) {
            // 엑세스 토큰이 없는 경우 처리
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                throw new AccessTokenNotFoundException(); // -> 프론트에서 엑세스 토큰과 리프레시 토큰을 버려야 함.
            }

            // 엑세스 토큰이 유효하지 않은 경우 처리
            if (!JwtUtil.isValidateToken(token)) {
                throw new TokenNotValidatedException(); // -> 프론트에서 엑세스 토큰과 리프레시 토큰을 버려야 함.
            }

            // 엑세스 토큰이 만료된 경우 처리
            if (JwtUtil.isExpiredToken(token)) {
                throw new AccessTokenExpiredException(); // -> 프론트에서 리프레시 토큰을 이용해 엑세스 토큰을 다시 발급 받아야 함.
            }
        }

        // 토큰이 필요하지 않거나 정상적으로 들어 있는 경우
        return true;
    }
}