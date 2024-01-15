package damon.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Travel {
    @Id
    @Column(name = "travel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    private String locationName;

    private String latitude;

    private String longitude;

    private int orderNum;

    @Builder
    public Travel(Calendar calendar, String locationName, String latitude, String longitude, int orderNum) {
        this.calendar = calendar;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderNum = orderNum;
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        if (calendar != null) {
            calendar.getTravels().add(this);
        }
    }
}