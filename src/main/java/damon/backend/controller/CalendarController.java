package damon.backend.controller;

import damon.backend.dto.request.CalendarCreateRequestDto;
import damon.backend.dto.request.CalendarEditRequestDto;
import damon.backend.dto.request.CalendarsDeleteRequestDto;
import damon.backend.dto.response.CalendarCreateResponseDto;
import damon.backend.dto.response.CalendarEditResponseDto;
import damon.backend.dto.response.CalendarResponseDto;
import damon.backend.dto.response.CalendarsResponseDto;
import damon.backend.dto.response.user.KakaoUserDto;
import damon.backend.service.CalendarService;
import damon.backend.util.auth.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public CalendarCreateResponseDto createCalendar(
            @RequestBody CalendarCreateRequestDto calendarCreateRequestDto,
            @AuthToken KakaoUserDto kakaoUserDto
    ) {
        return calendarService.createCalendar(kakaoUserDto.getIdentifier(), calendarCreateRequestDto);
    }

    @Operation(summary = "내 일정 리스트 조회", description = "내 일정리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 리스트 조회 성공")
    @GetMapping("/my/calendar")
    public Page<CalendarsResponseDto> getCalendars(
            @Schema(description = "페이지 번호(0부터 N까지)", defaultValue = "0")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam(name = "size",defaultValue = "10") int size,
            @AuthToken KakaoUserDto kakaoUserDto
    ) {
        // 프론트 메인페이지에서 해당 api를 호출하고 있는데(사실 프론트 쪽에서 고쳐야합니다.)
        // 그 때문에 페이지 에러가 나 calendarRepository.findPageByUser 안에 쿼리 수정하였습니다.
        // 죄송합니다. 나중에 수정할게요.
//        return calendarService.getCalendars(kakaoUserDto.getIdentifier(), page, size);
        return calendarService.getCalendars(page, size);
    }

    @Operation(summary = "내 일정 상세 조회", description = "내 일정 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 상세 조회 성공")
    @GetMapping("/my/calendar/{calendarId}")
    public CalendarResponseDto getCalendar(
            @Schema(description = "조회 할 일정 상세 페이지 ID", example="1")
            @PathVariable("calendarId") Long calendarId,
            @AuthToken KakaoUserDto kakaoUserDto
    ) {
        return calendarService.getCalendar(kakaoUserDto.getIdentifier(), calendarId);
    }

    @Operation(summary = "내 일정 상세 수정", description = "내 일정 상세 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "일정 수정 성공")
    @PutMapping("/calendar/{calendarId}")
    public CalendarEditResponseDto updateCalendar(
            @Schema(description = "수정 할 일정 상세 페이지 ID", example="1")
            @PathVariable("calendarId") Long calendarId,
            @RequestBody CalendarEditRequestDto calendarEditRequestDto,
            @AuthToken KakaoUserDto kakaoUserDto
    ) {
        return calendarService.updateCalendar(kakaoUserDto.getIdentifier(), calendarId, calendarEditRequestDto);
    }

    @Operation(summary = "내 일정 삭제", description = "내 일정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 삭제 성공")
    @DeleteMapping("/calendar/{calendarId}")
    public void deleteCalendar(
            @Schema(description = "삭제 할 일정 상세 페이지 ID", example="1")
            @PathVariable("calendarId") Long calendarId,
            @AuthToken KakaoUserDto kakaoUserDto
    ) {
        calendarService.deleteCalendar(kakaoUserDto.getIdentifier(), calendarId);
    }

    @Operation(summary = "내 일정 선택 삭제", description = "내 일정을 선택 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 선택 삭제 성공")
    @DeleteMapping("/calendar")
    public void deleteCalendars(
            @RequestBody CalendarsDeleteRequestDto calendarsDeleteRequestDto,
            @AuthToken KakaoUserDto kakaoUserDto
    ) {
        calendarService.deleteCalendars(kakaoUserDto.getIdentifier(), calendarsDeleteRequestDto);
    }
}