package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.user.LoginDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.service.UserService;
import damon.backend.util.login.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @GetMapping("/login/kakao")
    public Result<LoginDto> loginByKakao(@RequestParam("code") String code) {
        return Result.success(userService.loginByKakao(code));
    }

    @Operation(summary = "네이버 로그인")
    @GetMapping("/login/naver")
    public Result<LoginDto> loginByNaver(@RequestParam("code") String code) {
        return Result.success(userService.loginByNaver(code));
    }


    @Operation(summary = "토큰으로 유저 정보 조회")
    @GetMapping("/info")
    public Result<UserDto> getUserInfo(
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        return Result.success(userService.getUserInfo(identifier));
    }

    @Operation(summary = "리프레시 토큰으로 엑세스 토큰 재발급 + 리프레시 토큰 유효기간 갱신")
    @PostMapping("/refresh")
    public Result<LoginDto> refresh(@RequestParam("refreshToken") String refreshToken) {
        return Result.success(userService.refresh(refreshToken));
    }

    // 유저 닉네임 변경 API
    @PatchMapping("/updateNickname")
    @Operation(summary = "유저 닉네임 변경")
    public Result<UserDto> updateNickname(
            @AuthToken String identifier,
            @RequestParam("newNickname") String newNickname) {
        UserDto updatedUser = userService.updateNickname(identifier, newNickname);
        return Result.success("User nickname successfully updated.", updatedUser);
    }


    // 유저 탈퇴 API
    @DeleteMapping("/delete")
    @Operation(summary = "유저 탈퇴")
    public Result<?> deleteUser(@AuthToken String identifier) {
        userService.deleteUserAccount(identifier);
        return Result.success("User successfully deleted.");
    }

}