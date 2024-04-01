package damon.backend.dto.response.user;

import lombok.Data;

@Data
public class LoginTokenDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

    public LoginTokenDto(String access_token, String token_type, String refresh_token, long expires_in, String scope) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
    }
}