package damon.backend.util.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import damon.backend.dto.response.user.LoginTokenDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.exception.custom.LoginAuthFailException;
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
 * 네이버 API와 통신하여 토큰 및 사용자 정보를 가져오는 유틸리티 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class NaverUtil {

    private final Environment env;

    // 인가 코드로 카카오 토큰 발급
    public LoginTokenDto getNaverToken(String code) {
        try {
            String naverTokenUrl = "https://nid.naver.com/oauth2.0/token";
            String grant_type = "authorization_code";
            String naverClientId = env.getProperty("naver.client-id");
            String naverClientSecret = env.getProperty("naver.client-secret");
            String naverRedirectUri = env.getProperty("naver.redirect-uri");

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(naverTokenUrl)
                    .queryParam("grant_type", grant_type)
                    .queryParam("client_id", naverClientId)
                    .queryParam("client_secret", naverClientSecret)
                    .queryParam("redirect_uri", naverRedirectUri)
                    .queryParam("code", code);

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(builder.toUriString(), null, LoginTokenDto.class);
        } catch (Exception e) {
            throw new LoginAuthFailException();
        }
    }

    public UserDto getNaverUser(String accessToken) {
        try {
            String reqURL = "https://openapi.naver.com/v1/nid/me";

            JsonObject jsonObject = getJsonObject(accessToken, reqURL);

            JsonObject response = jsonObject.getAsJsonObject("response");
            String identifier = response.get("id").getAsString();
            String nickname = response.get("name").getAsString();
            String profile = response.get("profile_image").getAsString();

            // 이메일 허용 x 시 빈 문자열 받도록 처리
            String email = "";
            if (response.has("email")) {
                email = response.get("email").getAsString();
            }

            return new UserDto(identifier, nickname, email, profile);
        } catch (Exception e) {
            throw new LoginAuthFailException();
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
