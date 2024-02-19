package damon.backend.dto.response.user;

import lombok.Data;

import java.util.Date;

@Data
public class KakaoUserDto {
    private String identifier;
    private String nickname;
    private String email;
    private String profile;
    private Date expiryDate;

    public KakaoUserDto(String identifier, String nickname, String email, String profile) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
    }

    public KakaoUserDto(String identifier, String nickname, String email, String profile, Date expiryDate) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
        this.expiryDate = expiryDate;
    }
}