package damon.backend.dto.response.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class ChatRoomNotificationDTO {
    private String roomId;
    private String topic;

    public static ChatRoomNotificationDTO of(String roomId, String topic) {
        return new ChatRoomNotificationDTO(roomId, topic);
    }
}
