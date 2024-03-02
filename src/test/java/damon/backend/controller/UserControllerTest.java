package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.user.LoginDto;
import damon.backend.dto.response.user.UserDto;
import damon.backend.entity.community.Community;
import damon.backend.entity.user.User;
import damon.backend.enums.CommunityType;
import damon.backend.repository.community.CommunityRepository;
import damon.backend.repository.user.UserRepository;
import damon.backend.util.login.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Slf4j
@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @BeforeEach
    void beforeEach() {
        User user = userRepository.save(new User("1", "장성준", "", ""));
        Community community = communityRepository.save(new Community(user, CommunityType.자유, "community title", "community content"));
    }

    @Test
    void getUserInfo() {
        Result<UserDto> result = userController.getUserInfo("1");
        assertAll(
                () -> assertEquals(result.getStatus(), "200 OK"),
                () -> assertNull(result.getMessage())
        );
    }

    @Test
    void refresh() {
        LoginDto loginDto = new LoginDto(JwtUtil.generateAccessToken("1"), JwtUtil.generateRefreshToken("1"));
        Result<LoginDto> result = userController.refresh(loginDto.getRefreshToken());
        assertAll(
                () -> assertEquals(result.getStatus(), "200 OK"),
                () -> assertNull(result.getMessage())
        );
    }
}