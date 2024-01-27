package damon.backend.exception;

public class NotMeException extends RuntimeException {

    public NotMeException() {
        super("본인이 아닙니다.");
    }
}