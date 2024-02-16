package damon.backend.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import damon.backend.dto.response.user.KakaoTokenDto;
import damon.backend.dto.response.user.KakaoUserDto;
import damon.backend.entity.user.User;
import damon.backend.repository.user.UserRepository;
import damon.backend.util.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public String kakaoLogin(String code) {
        KakaoTokenDto token = getKakaoToken(code); // 인가 코드로 카카오 토큰 발급
        KakaoUserDto kakaoUser = getKakaoUser(token.getAccess_token()); // 카카오 엑세스 토큰으로 카카오 유저 정보 조회

        // DB에 없으면 회원가입
        if (userRepository.findByIdentifier(kakaoUser.getIdentifier()).orElse(null) == null) {
            kakaoSignUp(kakaoUser);
        }

        String serverToken = Jwt.generateServerToken(
                kakaoUser.getIdentifier(),
                kakaoUser.getNickname(),
                kakaoUser.getEmail(),
                kakaoUser.getProfile()
        );

        log.info("kakaoLogin code={}", code);
        log.info("kakaoLogin KakaoTokenDto={}", token);
        log.info("kakaoLogin KakaoUserDto={}", kakaoUser);
        log.info("kakaoLogin serverToken={}", serverToken);
        return serverToken;
    }

    // 인가 코드로 카카오 토큰 발급
    public KakaoTokenDto getKakaoToken(String code) {
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
        String grantType = "authorization_code";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoTokenUrl)
                .queryParam("grant_type", grantType)
                .queryParam("client_id", kakaoClientId)
                .queryParam("client_secret", kakaoClientSecret)
                .queryParam("redirect_uri", kakaoRedirectUri)
                .queryParam("code", code);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(builder.toUriString(), null, KakaoTokenDto.class);
        // 인가코드 하나로 2번째 요청 시 postForObject에서 오류 발생
        // 400 Bad Request: "{"error":"invalid_grant","error_description":"authorization code not found for code=","error_code":"KOE320"}"
    }

    // 카카오 엑세스 토큰으로 카카오 유저 정보 조회
    public KakaoUserDto getKakaoUser(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            JsonObject jsonObject = getJsonObject(access_Token, reqURL);
            String id = jsonObject.get("id").getAsString();

            JsonObject properties = jsonObject.getAsJsonObject("properties");
            String nickname = properties.get("nickname").getAsString();
            String profile = properties.get("profile_image").getAsString();

            KakaoUserDto kakaoUser = new KakaoUserDto(id, nickname, "", profile);
            return kakaoUser;
        } catch (IOException e) {
            return null;
        }
    }

    public void kakaoSignUp(KakaoUserDto kakaoUser) {
        User user = userRepository.save(new User(
                kakaoUser.getIdentifier(),
                kakaoUser.getNickname(),
                kakaoUser.getEmail(),
                kakaoUser.getProfile()
        ));
        log.info("kakaoSignUp {}", user);
    }

    public KakaoUserDto getKakaoUserDtoByServerToken(String serverToken) {
        KakaoUserDto kakaoUser = Jwt.getKakaoUserDtoByServerToken(serverToken);
        log.info("kakaoLogin serverToken={}", serverToken);
        log.info("kakaoLogin KakaoUserDto={}", kakaoUser);
        return kakaoUser;
    }

    private JsonObject getJsonObject(String access_Token, String reqURL) throws IOException {
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + access_Token);

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
