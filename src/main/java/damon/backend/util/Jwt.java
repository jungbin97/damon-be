package damon.backend.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import damon.backend.dto.response.user.TokenDto;
import damon.backend.dto.response.user.UserDto;
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
    public static String generateToken(UserDto userDto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userDto.getIdentifier())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static TokenDto getUserDtoByToken(String token) {

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
    }
}
