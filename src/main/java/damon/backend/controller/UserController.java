package damon.backend.controller;

import damon.backend.dto.response.user.KakaoUserDto;
import damon.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @RequestMapping("/login/oauth2/code/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(userService.kakaoLogin(code));
    }

    @RequestMapping("/user/info")
    public ResponseEntity<KakaoUserDto> getKakaoUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getKakaoUserDtoByServerToken(token));
    }
}
