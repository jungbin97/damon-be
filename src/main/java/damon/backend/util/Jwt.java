package damon.backend.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import damon.backend.dto.response.user.KakaoUserDto;
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
        // 토큰을 점으로 나누어 Header, Payload, Signature로 분리
        String[] parts = token.split("\\.");

        // Payload 부분을 디코딩하여 클레임을 추출
        byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);

        // 추출한 클레임을 JSON 객체로 파싱
        JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();

        // 클레임에서 필요한 정보 추출
        String id = payloadJson.get("sub").getAsString(); // 사용자 아이디
        String nickname = payloadJson.get("nickname").getAsString(); // 닉네임
        String email = payloadJson.get("email").getAsString(); // 이메일
        String profileUrl = payloadJson.get("profileUrl").getAsString(); // 프로필 URL
        Long expiryDate = payloadJson.get("exp").getAsLong(); // 프로필 URL

        return new KakaoUserDto(id, nickname, email, profileUrl, new Date(expiryDate));
    }
}
