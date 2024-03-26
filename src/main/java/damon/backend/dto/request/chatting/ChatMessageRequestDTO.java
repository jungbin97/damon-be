package damon.backend.dto.request.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class ChatMessageRequestDTO {
    private String sender;
    private String content;

    public static ChatMessageRequestDTO of(String sender, String content) {
        return new ChatMessageRequestDTO(sender, content);
    }
}
