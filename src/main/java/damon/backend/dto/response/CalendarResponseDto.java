package damon.backend.dto.response;

import damon.backend.entity.Area;
import damon.backend.entity.Calendar;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CalendarResponseDto {
    private String title;
    private String startDate;
    private String endDate;
    private Area area;

    private List<TravelDetailDto> travels;

    public static CalendarResponseDto from(Calendar calendar){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new CalendarResponseDto(
                calendar.getTitle(),
                calendar.getStartDate().format(formatter),
                calendar.getEndDate().format(formatter),
                calendar.getArea(),
                TravelDetailDto.listFrom(calendar.getTravels())
        );
    }
}
