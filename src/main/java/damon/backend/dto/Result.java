package damon.backend.dto;

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
        return new Result<>("success", null, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>("success", message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>("error", message, null);
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