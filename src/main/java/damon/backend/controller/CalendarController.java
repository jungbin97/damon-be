package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.request.CalendarCreateRequestDto;
import damon.backend.dto.request.CalendarEditRequestDto;
import damon.backend.dto.request.CalendarsDeleteRequestDto;
import damon.backend.dto.response.CalendarCreateResponseDto;
import damon.backend.dto.response.CalendarEditResponseDto;
import damon.backend.dto.response.CalendarResponseDto;
import damon.backend.dto.response.CalendarsResponseDto;
import damon.backend.service.CalendarService;
import damon.backend.util.login.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "일정 API", description = "일정 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CalendarController {
    private final CalendarService calendarService;


    @PostMapping("/calendar")
    @Operation(summary = "내 일정 등록", description = "내 일정을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "일정 등록 성공")
//    @ApiResponse(responseCode = "400", description = "일정 등록 실패")
    public Result<CalendarCreateResponseDto> createCalendar(
            @RequestBody CalendarCreateRequestDto calendarCreateRequestDto,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        CalendarCreateResponseDto calendar = calendarService.createCalendar(identifier, calendarCreateRequestDto);
        return Result.success(calendar);
    }

    @Operation(summary = "상위 5개 리스트 조회", description = "상위 5개 일정리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 리스트 조회 성공")
    @GetMapping("/top5/calendar")
    public Result<List<CalendarsResponseDto>> getCalendarsTop5() {
        List<CalendarsResponseDto> calendarsTop5 = calendarService.getCalendarsTop5();
        return Result.success(calendarsTop5);
    }

    @Operation(summary = "내 일정 리스트 조회", description = "내 일정리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 리스트 조회 성공")
    @GetMapping("/my/calendar")
    public Result<Page<CalendarsResponseDto>> getCalendars(
            @Schema(description = "페이지 번호(0부터 N까지)", defaultValue = "0")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam(name = "size",defaultValue = "10") int size,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier

    ) {
        Page<CalendarsResponseDto> calendars = calendarService.getCalendars(identifier, page, size);
        return Result.success(calendars);
    }

    @Operation(summary = "내 일정 상세 조회", description = "내 일정 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 상세 조회 성공")
    @GetMapping("/my/calendar/{calendarId}")
    public Result<CalendarResponseDto> getCalendar(
            @Schema(description = "조회 할 일정 상세 페이지 ID", example="1")
            @PathVariable("calendarId") Long calendarId,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        CalendarResponseDto calendar = calendarService.getCalendar(identifier, calendarId);
        return Result.success(calendar);
    }

    @Operation(summary = "내 일정 상세 수정", description = "내 일정 상세 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "일정 수정 성공")
    @PutMapping("/calendar/{calendarId}")
    public Result<CalendarEditResponseDto> updateCalendar(
            @Schema(description = "수정 할 일정 상세 페이지 ID", example="1")
            @PathVariable("calendarId") Long calendarId,
            @RequestBody CalendarEditRequestDto calendarEditRequestDto,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        CalendarEditResponseDto calendarEditResponseDto = calendarService.updateCalendar(identifier, calendarId, calendarEditRequestDto);
        return Result.success(calendarEditResponseDto);
    }

    @Operation(summary = "내 일정 삭제", description = "내 일정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 삭제 성공")
    @DeleteMapping("/calendar/{calendarId}")
    public Result<Boolean> deleteCalendar(
            @Schema(description = "삭제 할 일정 상세 페이지 ID", example="1")
            @PathVariable("calendarId") Long calendarId,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        calendarService.deleteCalendar(identifier, calendarId);
        return Result.success(true);
    }

    @Operation(summary = "내 일정 선택 삭제", description = "내 일정을 선택 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 선택 삭제 성공")
    @DeleteMapping("/calendar")
    public Result<Boolean> deleteCalendars(
            @RequestBody CalendarsDeleteRequestDto calendarsDeleteRequestDto,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        calendarService.deleteCalendars(identifier, calendarsDeleteRequestDto);
        return Result.success(true);
    }
}