package damon.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import damon.backend.dto.response.LoginResponse;
import damon.backend.entity.AuthTokens;
import damon.backend.entity.Member;
import damon.backend.exception.KakaoLoginException;
import damon.backend.jwt.AuthTokensGenerator;
import damon.backend.jwt.JwtTokenProvider;
import damon.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.key.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;


    public LoginResponse kakaoLogin(String code) {
        log.info("kakaoLogin 로직 실행");
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo = getKakaoUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponse kakaoUserResponse = kakaoUserLogin(userInfo);
        log.info("끝");

        return kakaoUserResponse;
    }

    //1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) {
        try {
            log.info("getAccessToken 실행");

            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP Body 생성
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
            body.add("redirect_uri", redirectUri);
            body.add("code", code);

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            log.info("http요청 보냄");

            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            log.info("http응답받음");
            try {
                jsonNode = objectMapper.readTree(responseBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return jsonNode.get("access_token").asText(); //토큰 전송
        } catch (HttpStatusCodeException e) {
            throw new KakaoLoginException("인가 코드로 카카오 토큰 발급 중 예외 발생");
        }
    }

    //2. 토큰으로 카카오 API 호출
    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        try {
            log.info("getKakaoUserInfo 실행");
            HashMap<String, Object> userInfo = new HashMap<String, Object>();

            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );

            // responseBody에 있는 정보를 꺼냄
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(responseBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            Long id = jsonNode.get("id").asLong();
            String email = jsonNode.get("kakao_account").get("email").asText();
            String nickname = jsonNode.get("properties").get("nickname").asText();

            userInfo.put("id", id);
            userInfo.put("email", email);
            userInfo.put("nickname", nickname);

            return userInfo;
        } catch (RuntimeException e2) {
            throw new KakaoLoginException("토큰으로 카카오 API 호출 중 예외 발생");
        }
    }

    //3. 카카오ID로 회원가입 & 로그인 처리
    private LoginResponse kakaoUserLogin(HashMap<String, Object> userInfo) {
        log.info("kakaoUserLogin 실행");

        Long id = Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        Member kakaoUser = memberRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoUser == null) {    //회원가입
            kakaoUser = new Member();
            kakaoUser.setId(id);
            kakaoUser.setName(nickName);
            kakaoUser.setEmail(kakaoEmail);
            memberRepository.save(kakaoUser);
        }
        //토큰 생성
        AuthTokens token = authTokensGenerator.generate(id.toString());
        return new LoginResponse(id, nickName, kakaoEmail, token);
    }

}