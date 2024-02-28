package damon.backend.util;

import damon.backend.dto.response.user.TokenDto;
import damon.backend.exception.custom.TokenNotValidatedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class Jwt {

    // 안전한 랜덤 키 생성
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 토큰 유효 시간 (30분)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    // 리프래시 토큰 유효 시간 (7일)
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    // JWT 토큰 생성
    public static String generateAccessToken(String identifier) {
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
        try {
            // 토큰의 유효성 검증 및 페이로드 추출
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            // 필요한 클레임 추출
            String identifier = claims.getSubject(); // 사용자 아이디
            Date expiryDate = claims.getExpiration(); // 토큰 만료 일자

            return new TokenDto(identifier, expiryDate);
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰 검증 실패 시
            throw new TokenNotValidatedException();
        }
    }

    public static boolean isExpiredToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            Date expiryDate = claims.getExpiration();

            // 토큰 만료 여부 확인
            return expiryDate.before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenNotValidatedException();
        }
    }

    public static boolean isNotValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return false;

        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}
