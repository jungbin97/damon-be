package damon.backend.exception;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("토큰의 유효기간이 만료되었습니다.");
    }
}
