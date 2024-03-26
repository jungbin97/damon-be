package damon.backend.dto.response;

import damon.backend.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserResponseDTO {
    private String identifier;
    private String nickname;
    private String email;
    private String profile;

    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getIdentifier(), user.getNickname(), user.getEmail(), user.getProfile());
    }
}
