package damon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ChatRoomModel implements Serializable {
    private String roomId;
    private String participantOne;
    private String participantTwo;
    private LocalDateTime createTime;
}