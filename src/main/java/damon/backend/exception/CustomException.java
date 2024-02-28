package damon.backend.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final int code;
    private final String status;
    private final String errorMessage;

    public CustomException(int code, String status, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
