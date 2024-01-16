package damon.backend.dto.response;

import damon.backend.entity.Travel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TravelDetailDto {
    private int day;
    private String localName;
    private String latitude;
    private String longitude;

    private String memo;

    public static TravelDetailDto form(Travel travel) {
        return new TravelDetailDto(
                travel.getOrderNum(),
                travel.getLocationName(),
                travel.getLatitude(),
                travel.getLongitude(),
                travel.getMemo()
        );
    }

    public static List<TravelDetailDto> listFrom(List<Travel> travels) {
        return travels.stream().map(TravelDetailDto::form).toList();
    }
}
