package damon.backend.util;

import damon.backend.dto.response.user.KakaoUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class Jwt {

    // 안전한 랜덤 키 생성
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 토큰 유효 시간 (30분)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    // JWT 토큰 생성
    public static String generateServerToken(String userId, String nickname, String email, String profileUrl) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userId) // 사용자 아이디를 subject로 설정
                .claim("nickname", nickname) // 닉네임을 클레임에 추가
                .claim("email", email) // 이메일을 클레임에 추가
                .claim("profileUrl", profileUrl) // 프로필 URL을 클레임에 추가
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // JWT 토큰에서 사용자 정보 추출하여 KakaoUserDto 객체로 반환
    public static KakaoUserDto getKakaoUserDtoByServerToken(String token) {
        // 토큰에서 클레임을 추출하여 KakaoUserDto 객체를 생성하여 반환
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String id = claims.getSubject();
        String nickname = (String) claims.get("nickname");
        String email = (String) claims.get("email");
        String profileUrl = (String) claims.get("profileUrl");
        return new KakaoUserDto(id, nickname, email, profileUrl);
    }
}
