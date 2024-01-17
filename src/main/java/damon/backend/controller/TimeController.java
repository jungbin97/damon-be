package damon.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TimeController {

    @GetMapping("/time")
    @Operation(summary = "서버 테스트 용 시간체크" , description = "현재 시간을 반환합니다.")
    public String getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);
    }
}