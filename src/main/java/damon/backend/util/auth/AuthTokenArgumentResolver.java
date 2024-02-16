package damon.backend.util.auth;

import damon.backend.util.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthToken.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // 헤더에서 토큰 추출하고 해당 토큰을 이용하여 사용자 정보를 얻는 로직을 여기에 구현
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 부분을 제외한 토큰 추출
            // userService.getKakaoUserDtoByServerToken(token)을 호출하여 KakaoUserDto를 반환
            return Jwt.getKakaoUserDtoByServerToken(token);
        }
        return null;
    }
}