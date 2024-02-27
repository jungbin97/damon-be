package damon.backend.exception;

public class TokenNotValidateException extends RuntimeException {

    public TokenNotValidateException() {
        super("잘못된 토큰 정보입니다.");
    }
}
