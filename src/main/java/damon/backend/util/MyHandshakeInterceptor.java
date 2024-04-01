package damon.backend.util;

import damon.backend.exception.custom.AccessTokenNotFoundException;
import damon.backend.util.login.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 요청 해더에서 "Authorization" 키의 값을 가져옵니다.
        String token = request.getHeaders().getFirst("Authorization");

        // 토큰이 없거나 빈 문자열이라면 예외를 던집니다.
        if (token == null || token.isEmpty()) {
            throw new AccessTokenNotFoundException();
        }

        // 토큰이 "Bearer "로 시작한다면 실제 토큰 부분을 분리합니다.
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 여기에 JWT 토큰을 검증하고, 유효한 경우 사용자 식별자를 추출합니다.
        // JwtUtil은 사용자의 토큰 검증 로직을 포함한 유틸리티 클래스입니다.
        try {
            String identifier = JwtUtil.extractAtkIdentifier(token);
            attributes.put("identifier", identifier);   // WebSocket 세션 속성에 사용자 ID를 추가합니다.
        } catch (Exception e) {
            log.error("토큰 검증 실패: {}", e.getMessage());
            throw new AccessTokenNotFoundException();
        }

        // 핸드세이크 과정이 계속 진행되도록 true를 반환합니다.
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
