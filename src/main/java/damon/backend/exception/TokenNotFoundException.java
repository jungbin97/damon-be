package damon.backend.exception;

public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException() {
        super("토큰이 존재하지 않습니다.");
    }
}