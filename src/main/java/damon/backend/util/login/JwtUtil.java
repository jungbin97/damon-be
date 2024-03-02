package damon.backend.util.login;

import damon.backend.exception.custom.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰을 생성하고 해석하는 유틸리티 클래스입니다.
 */
public class JwtUtil {

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

    public static String extractAtkIdentifier(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new AccessTokenNotFoundException();
        }

        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject();
        } catch (SignatureException | MalformedJwtException e) {
            throw new TokenNotValidatedException();
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        }
    }

    public static String extractRtkIdentifier(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RefreshTokenNotFoundException();
        }

        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(refreshToken)
                    .getBody()
                    .getSubject();
        } catch (SignatureException | MalformedJwtException e) {
            throw new TokenNotValidatedException();
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        }
    }
}
