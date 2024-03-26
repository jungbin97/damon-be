package damon.backend.dto.request.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class CreateRoomDTO {
    private String opponentId;

    public static CreateRoomDTO from(String opponentId) {
        return new CreateRoomDTO(opponentId);
    }
}
