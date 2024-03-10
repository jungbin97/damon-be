package damon.backend.service;

import damon.backend.dto.response.user.KakaoTokenDto;
import damon.backend.dto.response.user.LoginDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.entity.user.User;
import damon.backend.exception.CustomException;
import damon.backend.exception.Status;
import damon.backend.exception.custom.*;
import damon.backend.repository.user.UserRepository;
import damon.backend.util.login.JwtUtil;
import damon.backend.util.login.KakaoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoUtil kakaoUtil;

    public LoginDto loginByKakao(String code) {
        KakaoTokenDto token = kakaoUtil.getKakaoToken(code); // 인가 코드로 카카오 토큰 발급
        UserDto userDto = kakaoUtil.getKakaoUser(token.getAccess_token()); // 카카오 엑세스 토큰으로 유저 정보 조회

        // DB에 없으면 회원가입
        if (userRepository.findByIdentifier(userDto.getIdentifier()).isEmpty()) {
            signUp(userDto);
        }

        String accessToken = JwtUtil.generateAccessToken(userDto.getIdentifier());
        String refreshToken = JwtUtil.generateRefreshToken(userDto.getIdentifier());

        return new LoginDto(accessToken, refreshToken);
    }

    private void signUp(UserDto userDto) {
        userRepository.save(new User(userDto.getIdentifier(), userDto.getNickname(), userDto.getEmail(), userDto.getProfile()));
    }

    public UserDto getUserInfo(String identifier) {
        return new UserDto(userRepository.findByIdentifier(identifier).orElseThrow(DataNotFoundException::new));
    }

    public LoginDto refresh(String refreshToken) {
        String identifier = JwtUtil.extractRtkIdentifier(refreshToken);
        String newAccessToken = JwtUtil.generateAccessToken(identifier);
        String newRefreshToken = JwtUtil.generateRefreshToken(identifier);
        return new LoginDto(newAccessToken, newRefreshToken);
    }

// UserService.java

    public UserDto updateNickname(String identifier, String newNickname) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND.value(), Status.NOT_FOUND, "User not found with identifier: " + identifier));
        user.setNickname(newNickname);
        userRepository.save(user);
        return new UserDto(user);
    }


    public void deleteUserAccount(String identifier) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND.value(), Status.NOT_FOUND, "User not found with identifier: " + identifier));
        userRepository.delete(user);
    }

}
