package damon.backend.controller;

import damon.backend.dto.response.LoginResponse;
import damon.backend.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/code")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private final KakaoService kakaoService;
//    private final NaverService naverService;

    @GetMapping("/kakao")
    public String kakaoLogin(@RequestParam String code) {
        try {
            LoginResponse loginResponse = kakaoService.kakaoLogin(code);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setLocation(URI.create("http://localhost:3000")); // 클라이언트 주소
//            headers.add("Authorization", "Bearer " + loginResponse.getToken().getAccessToken()); // 토큰을 헤더에 추가
//            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER); // 303 SEE_OTHER 상태 코드 사용
            return loginResponse.getToken().getAccessToken();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}