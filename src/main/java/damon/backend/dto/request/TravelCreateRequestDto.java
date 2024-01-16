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
    private int orderNum;
    private String memo;

    public static TravelCreateRequestDto of(String locationName, String latitude, String longitude, int orderNum, String memo) {
        return new TravelCreateRequestDto(locationName, latitude, longitude, orderNum, memo);
    }
}
