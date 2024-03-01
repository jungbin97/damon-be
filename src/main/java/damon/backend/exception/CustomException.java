package damon.backend.exception;

import lombok.Getter;

/**
 * 사용자 정의 예외를 나타내는 클래스입니다.
 * 이 클래스는 예외의 코드, 상태, 그리고 에러 메시지를 포함합니다.
 */
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
