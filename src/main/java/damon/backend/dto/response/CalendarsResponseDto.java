package damon.backend.dto.response;

import damon.backend.entity.Area;
import damon.backend.entity.Calendar;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CalendarsResponseDto {
    private Long calendarId;
    private String title;
    private String startDate;
    private String endDate;
    private Area area;

    public static CalendarsResponseDto from(Calendar calendar) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new CalendarsResponseDto(
                calendar.getId(),
                calendar.getTitle(),
                calendar.getStartDate().format(formatter),
                calendar.getEndDate().format(formatter),
                calendar.getArea()
        );
    }

    public static List<CalendarsResponseDto> listFrom(List<Calendar> calendars) {
        return calendars.stream().map(CalendarsResponseDto::from).toList();
    }
}
