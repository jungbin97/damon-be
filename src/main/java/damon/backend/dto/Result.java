package damon.backend.dto;

import org.springframework.http.HttpStatus;

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
        return new Result<>(HttpStatus.OK.toString(), null, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(HttpStatus.OK.toString(), message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.toString(), message, null);
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}