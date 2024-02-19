package damon.backend.dto.response.user;

import lombok.Data;

@Data
public class TokenDto {
    private String identifier;
    private Long expiryDate;

    public TokenDto(String identifier, Long expiryDate) {
        this.identifier = identifier;
        this.expiryDate = expiryDate;
    }
}