package damon.backend.util.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import damon.backend.dto.response.user.KakaoTokenDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.exception.custom.KakaoAuthFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 카카오 API와 통신하여 토큰 및 사용자 정보를 가져오는 유틸리티 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class KakaoUtil {

    private final Environment env;

    // 인가 코드로 카카오 토큰 발급
    public KakaoTokenDto getKakaoToken(String code) {
        try {
            String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
            String grantType = "authorization_code";
            String kakaoClientId = env.getProperty("kakao.client-id");
            String kakaoClientSecret = env.getProperty("kakao.client-secret");
            String kakaoRedirectUri = env.getProperty("kakao.redirect-uri");

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoTokenUrl)
                    .queryParam("grant_type", grantType)
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("client_secret", kakaoClientSecret)
                    .queryParam("redirect_uri", kakaoRedirectUri)
                    .queryParam("code", code);

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(builder.toUriString(), null, KakaoTokenDto.class);
        } catch (Exception e) {
            throw new KakaoAuthFailException();
        }
    }

    public UserDto getKakaoUser(String accessToken) {
        try {
            String reqURL = "https://kapi.kakao.com/v2/user/me";

            JsonObject jsonObject = getJsonObject(accessToken, reqURL);
            String identifier = jsonObject.get("id").getAsString();

            JsonObject properties = jsonObject.getAsJsonObject("properties");
            String nickname = properties.get("nickname").getAsString();
            String profile = properties.get("profile_image").getAsString();

            // 이메일 허용 x 시 빈 문자열 받도록 처리
            JsonObject kakaoAccount = jsonObject.getAsJsonObject("kakao_account");
            String email = "";
            if (kakaoAccount.has("email")) {
                email = kakaoAccount.get("email").getAsString();
            }

            return new UserDto(identifier, nickname, email, profile);
        } catch (Exception e) {
            throw new KakaoAuthFailException();
        }
    }

    private JsonObject getJsonObject(String accessToken, String reqURL) throws IOException {
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result.toString());

        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject;
    }
}
