package damon.backend.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Area {
    GAPYEONG("가평"),
    GANGWON("강원"),
    GEYONGGI("경기"),
    INCHEON("인천"),
    SEOUL("서울"),
    DAEJEON("대전"),
    CHUNGCHEONG("충청"),
    GYEONGSANG("경상"),
    DAEGU("대구"),
    BUSAN("부산"),
    ULSAN("울산"),
    YEOSU("여수"),
    GWANGJU("광주"),
    JEOLLLA("전라"),
    JEJU("제주");

    private final String description;

    public String getDescription(){
        return  description;
    }

}
