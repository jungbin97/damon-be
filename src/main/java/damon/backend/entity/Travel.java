package damon.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Calendar calendar;

    private String locationName;

    private String latitude;

    private String longitude;

    private String memo;

    private int travelDay;

    private int orderNumber;

    @Builder
    public Travel(Calendar calendar, String locationName, String latitude, String longitude, int travelDay, String memo, int orderNumber) {
        this.calendar = calendar;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.travelDay = travelDay;
        this.memo = memo;
        this.orderNumber = orderNumber;
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        if (calendar != null) {
            calendar.getTravels().add(this);
        }
    }

    public void update(String locationName, String latitude, String longitude, String memo, int travelDay, int orderNumber) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
        this.travelDay = travelDay;
        this.orderNumber = orderNumber;
    }
}