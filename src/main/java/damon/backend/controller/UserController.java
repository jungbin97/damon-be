package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.user.KakaoUserDto;
import damon.backend.exception.KakaoLoginException;
import damon.backend.service.UserService;
import damon.backend.util.auth.AuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam("code") String code) {
        try {
            return userService.kakaoLogin(code);
        } catch (Exception e) {
            throw new KakaoLoginException("카카오 로그인 중 예외 발생");
        }
    }

    @GetMapping("/api/user/info")
    public Result<KakaoUserDto> getKakaoUser(@AuthToken KakaoUserDto kakaoUserDto) {
        return Result.success(kakaoUserDto);
    }

//    @GetMapping("/api/user/info")
//    public Result<KakaoUserDto> getKakaoUser(@RequestHeader("Authorization") String token) {
//        return Result.success(userService.getKakaoUserDtoByServerToken(token));
//    }
}