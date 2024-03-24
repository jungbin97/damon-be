package damon.backend.dto.response.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class ChatMessageResponseDTO {
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public static ChatMessageResponseDTO of(String sender, String content, LocalDateTime timestamp) {
        return new ChatMessageResponseDTO(sender, content, timestamp);
    }
}
