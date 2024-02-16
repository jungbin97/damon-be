package damon.backend.dto.response.user;

import lombok.Data;

@Data
public class KakaoUserDto {
    private String identifier;
    private String nickname;
    private String email;
    private String profile;

    public KakaoUserDto(String identifier, String nickname, String email, String profile) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
    }
}