package damon.backend.exception;

public class KakaoLoginException extends RuntimeException {

    public KakaoLoginException(String message) {
        super(message);
    }
}