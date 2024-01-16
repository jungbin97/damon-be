package damon.backend.dto.request;

import damon.backend.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class CalendarCreateRequestDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Area area;
    private List<TravelCreateRequestDto> travels;


    public static CalendarCreateRequestDto of(String title, LocalDate startDate, LocalDate endDate, Area area, List<TravelCreateRequestDto> travels) {
        return new CalendarCreateRequestDto(title, startDate, endDate, area, travels);
    }
}
