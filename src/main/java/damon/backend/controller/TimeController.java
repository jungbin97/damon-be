package damon.backend.controller;

import damon.backend.service.TimeService;
import damon.backend.util.trace.LogTrace;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TimeController {

    private final LogTrace trace;
    private final TimeService timeService;

    @GetMapping("/time")
    @Operation(summary = "서버 테스트 용 시간체크", description = "현재 시간을 반환합니다.")
    public String getCurrentTime() {
        return timeService.getNowTime();
    }
}