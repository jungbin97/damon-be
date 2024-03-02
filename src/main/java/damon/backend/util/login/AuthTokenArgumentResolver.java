package damon.backend.util.login;

import damon.backend.exception.custom.AccessTokenNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.security.auth.login.AccountNotFoundException;

/**
 * 엑세스 토큰을 변환하는 클래스입니다.
 * extractAtkIdentifier 중에 토큰 관련 예외가 발생하면 관련된 커스텀 익셉션을 터트립니다.
 */
@Slf4j
public class AuthTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthToken.class) != null;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new AccessTokenNotFoundException();
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return JwtUtil.extractAtkIdentifier(token);
    }
}