package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.community.CommunitySimpleDTO;
import damon.backend.enums.CommunityType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class TimeControllerTest {

    @Autowired
    private TimeController timeController;

    @Test
    void getCurrentTime() {
        Result<String> result = timeController.getCurrentTime();
        assertAll(
                () -> assertEquals(result.getStatus(), "200 OK"),
                () -> assertNull(result.getMessage())
        );
    }
}