package damon.backend.util.auth;

import damon.backend.util.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// HttpServletRequest에서 Authorization 헤더에서 토큰을 추출하고,
// 추출된 토큰을 이용하여 Jwt.getUserDtoByToken 메서드를 호출하여 사용자 정보를 얻는 역활
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
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // 헤더에서 토큰 추출하고 해당 토큰을 이용하여 사용자 정보를 얻는 로직을 여기에 구현
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 부분을 제외한 토큰 추출
            return Jwt.getUserDtoByToken(token);
        } else if (token != null) {
            return Jwt.getUserDtoByToken(token);
        }
        return null;
    }
}