package damon.backend.dto.response.chatting;

import damon.backend.model.ChatMessageModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ChatRoomsResponseDTO {
    private String roomId;
    private String participantOne;
    private String participantTwo;
    private ChatMessageModel lastMessage;

    public static ChatRoomsResponseDTO of(String roomId, String participantOne, String participantTwo, ChatMessageModel lastMessage) {
        return new ChatRoomsResponseDTO(roomId, participantOne, participantTwo, lastMessage);
    }
}
