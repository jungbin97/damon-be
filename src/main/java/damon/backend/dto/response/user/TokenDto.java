package damon.backend.dto.response.user;

import lombok.Data;

import java.util.Date;

@Data
public class TokenDto {
    private String identifier;
    private Date expiryDate;

    public TokenDto(String identifier, Date expiryDate) {
        this.identifier = identifier;
        this.expiryDate = expiryDate;
    }
}