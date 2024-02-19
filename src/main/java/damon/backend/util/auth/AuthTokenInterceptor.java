package damon.backend.util.auth;

import damon.backend.exception.PermissionDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
                    throw new PermissionDeniedException("토큰이 없습니다.");
                }

                // 토큰이 잘못된 경우 처리
                if (!isValidToken(token)) {
                    throw new PermissionDeniedException("잘못된 토큰입니다.");
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 요청 처리 후, View로 변환하기 전에 수행할 작업
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 요청 처리가 완료된 후 수행할 작업
    }

    private boolean isValidToken(String token) {
        // 토큰의 유효성을 검사하는 로직
        // 유효한 토큰인 경우 true 반환, 그렇지 않은 경우 false 반환
        return true;
    }
}
