package damon.backend.dto;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;
import lombok.Getter;

/**
 * 결과를 표현하는 클래스입니다.
 * 이 클래스는 API 응답의 상태, 메시지, 데이터를 포함합니다.
 *
 * @param <T> 결과 데이터의 타입
 */
@Getter
public class Result<T> {

    private String status;
    private String message;
    private T data;

    private Result(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(Status.OK, null, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(Status.OK, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(Status.INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> Result<T> error(CustomException e) {
        return new Result<>(e.getStatus(), e.getErrorMessage(), null);
    }
}