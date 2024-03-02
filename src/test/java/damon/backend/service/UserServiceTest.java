package damon.backend.service;

import damon.backend.dto.response.user.LoginDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.entity.user.User;
import damon.backend.exception.custom.DataNotFoundException;
import damon.backend.repository.user.UserRepository;
import damon.backend.util.login.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void getUserInfo() {
        User user = userRepository.save(new User("1", "장성준", "", ""));
        assertEquals(userService.getUserInfo("1"), new UserDto(userRepository.findByIdentifier("1").orElseThrow(DataNotFoundException::new)));
    }

    @Test
    void refresh() {
        LoginDto firstLoginDto = new LoginDto(JwtUtil.generateAccessToken("1"), JwtUtil.generateRefreshToken("1"));
        LoginDto afterLoginDto = userService.refresh(firstLoginDto.getRefreshToken());

        assertAll(
                () -> assertEquals(JwtUtil.extractAtkIdentifier(firstLoginDto.getAccessToken()), JwtUtil.extractAtkIdentifier(afterLoginDto.getAccessToken())),
                () -> assertEquals(JwtUtil.extractRtkIdentifier(firstLoginDto.getRefreshToken()), JwtUtil.extractRtkIdentifier(afterLoginDto.getRefreshToken()))
        );
    }
}