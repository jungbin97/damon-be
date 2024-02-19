package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.user.TokenDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.exception.KakaoLoginException;
import damon.backend.service.UserService;
import damon.backend.util.auth.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 API", description = "회원 API")
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @Operation(summary = "카카오 로그인")
    @GetMapping("/login")
    public String kakaoLogin(@RequestParam("code") String code) {
        try {
            return userService.kakaoLogin(code);
        } catch (Exception e) {
            throw new KakaoLoginException("카카오 로그인 중 예외 발생");
        }
    }

    @Operation(summary = "토큰으로 유저 정보 조회")
    @GetMapping("/info")
    public Result<UserDto> getUserInfo(@AuthToken TokenDto tokenDto) {
        return Result.success(userService.getUserDto(tokenDto.getIdentifier()));
    }
}