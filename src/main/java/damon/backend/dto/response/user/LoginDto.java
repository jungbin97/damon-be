package damon.backend.dto.response.user;

import lombok.Data;

@Data
public class LoginDto {
    private String accessToken;
    private String refreshToken;

    public LoginDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
