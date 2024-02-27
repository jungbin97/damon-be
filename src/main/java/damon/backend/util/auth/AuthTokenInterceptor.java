package damon.backend.util.auth;

import damon.backend.dto.response.user.TokenDto;
import damon.backend.exception.TokenExpiredException;
import damon.backend.exception.TokenNotFoundException;
import damon.backend.exception.TokenNotValidateException;
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
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthToken authToken = handlerMethod.getMethodAnnotation(AuthToken.class);
            if (authToken != null) {

                // 토큰이 없는 경우 처리
                String token = request.getHeader("Authorization");
                if (token == null || token.isEmpty()) {
                    throw new TokenNotFoundException();
                }

                // 엑세스 토큰의 유효성 검사
                boolean isValidAccessToken = isValidToken(token);

                // 엑세스 토큰이 만료된 경우
                if (!isValidAccessToken) {
                    // 리프래시 토큰을 사용하여 새로운 엑세스 토큰 생성
                    String refreshToken = request.getHeader("Refresh-Token");
                    if (refreshToken != null && !refreshToken.isEmpty()) {
                        if (isValidToken(refreshToken)) {
                            // 리프래시 토큰이 유효한 경우, 새로운 엑세스 토큰 생성
                            String newAccessToken = generateNewAccessToken(refreshToken);

                            // 새로 생성한 엑세스 토큰을 응답 헤더에 설정
                            response.setHeader("New-Access-Token", newAccessToken);

                            // 요청 처리 허용
                            return true;
                        }
                    }
                    throw new TokenExpiredException();
                }
            }
        }
        return true;
    }

    private boolean isValidToken(String token) {
        // 토큰의 유효성을 검사하는 로직
        try {
            Jwt.getUserDtoByToken(token);
            return true;
        } catch (TokenExpiredException | TokenNotValidateException e) {
            return false;
        }
    }

    private String generateNewAccessToken(String refreshToken) {
        // 리프래시 토큰에서 사용자 정보 추출
        TokenDto tokenDto = Jwt.getUserDtoByToken(refreshToken);

        // 새로운 엑세스 토큰 생성
        String newAccessToken = Jwt.generateToken(tokenDto.getIdentifier());

        return newAccessToken;
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