package damon.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class TravelCreateRequestDto {
    private String locationName;
    private String latitude;
    private String longitude;
    private int day;
    private String memo;
    private int order;

    public static TravelCreateRequestDto of(String locationName, String latitude, String longitude, int day, String memo, int order){
        return new TravelCreateRequestDto(locationName, latitude, longitude, day, memo, order);
    }
}
