package damon.backend.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import damon.backend.dto.response.user.TokenDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.exception.PermissionDeniedException;
import damon.backend.exception.TokenExpiredException;
import damon.backend.exception.TokenNotValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class Jwt {

    // 안전한 랜덤 키 생성
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 토큰 유효 시간 (30분)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    // 리프래시 토큰 유효 시간 (7일)
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    // JWT 토큰 생성
    public static String generateToken(String identifier) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // JWT 리프래시 토큰 생성
    public static String generateRefreshToken(String identifier) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static TokenDto getUserDtoByToken(String token) {
        // 토큰의 유효성 검증 로직

        // 토큰의 유효 기간이 만료된 경우
        if (isTokenExpired(token)) {
            throw new TokenExpiredException();
        }

        try {
            // 토큰을 점으로 나누어 Header, Payload, Signature로 분리
            String[] parts = token.split("\\.");

            // Payload 부분을 디코딩하여 클레임을 추출
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            String payload = new String(payloadBytes, StandardCharsets.UTF_8);

            // 추출한 클레임을 JSON 객체로 파싱
            JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();

            // 클레임에서 필요한 정보 추출
            String identifier = payloadJson.get("sub").getAsString(); // 사용자 아이디
            Long expiryDate = payloadJson.get("exp").getAsLong(); // 프로필 URL

            return new TokenDto(identifier, new Date(expiryDate));
        } catch (Exception e) {
            throw new TokenNotValidateException();
        }
    }

    private static boolean isTokenExpired(String token) {
        try {
            // 토큰을 파싱하여 만료 일자 확인
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();

            // 현재 시간과 비교하여 토큰의 만료 여부 반환
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰 파싱 오류 또는 잘못된 인자로 인한 예외 발생 시
            return true;
        }
    }
}
