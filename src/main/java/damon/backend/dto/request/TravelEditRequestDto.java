package damon.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class TravelEditRequestDto {
    private Long travelId;
    private String locationName;
    private String latitude;
    private String longitude;
    private int orderNum;
    private String memo;
    private boolean deleted;



    public static TravelEditRequestDto of(Long travelId, String locationName, String latitude, String longitude, int orderNum, String memo, boolean deleted) {
        return new TravelEditRequestDto(travelId, locationName, latitude, longitude, orderNum, memo, deleted);
    }
}
