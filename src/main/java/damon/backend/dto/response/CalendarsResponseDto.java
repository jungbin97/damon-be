package damon.backend.dto.response;

import damon.backend.entity.Calendar;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CalendarsResponseDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    public static CalendarsResponseDto from(Calendar calendar) {
        return new CalendarsResponseDto(
                calendar.getId(),
                calendar.getTitle(),
                calendar.getStartDate(),
                calendar.getEndDate()
        );
    }

    public static List<CalendarsResponseDto> listFrom(List<Calendar> calendars) {
        return calendars.stream().map(CalendarsResponseDto::from).toList();
    }
}
