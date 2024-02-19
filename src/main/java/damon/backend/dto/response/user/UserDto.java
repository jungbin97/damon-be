package damon.backend.dto.response.user;

import damon.backend.entity.user.User;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String identifier;
    private String nickname;
    private String email;
    private String profile;

    public UserDto(String identifier, String nickname, String email, String profile) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.identifier = user.getIdentifier();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profile = user.getProfile();
    }
}
