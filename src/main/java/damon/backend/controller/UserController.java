package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.user.LoginDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.service.UserService;
import damon.backend.util.login.AuthToken;
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
    public Result<LoginDto> loginByKakao(@RequestParam("code") String code) {
        return Result.success(userService.loginByKakao(code));
    }

    @Operation(summary = "토큰으로 유저 정보 조회")
    @PostMapping("/info")
    public Result<UserDto> getUserInfo(@AuthToken String identifier) {
        return Result.success(userService.getUserInfo(identifier));
    }

    @Operation(summary = "리프레시 토큰으로 엑세스 토큰 재발급 + 리프레시 토큰 유효기간 갱신")
    @PostMapping("/refresh")
    public Result<LoginDto> refresh(@RequestParam("refreshToken") String refreshToken) {
        return Result.success(userService.refresh(refreshToken));
    }
}