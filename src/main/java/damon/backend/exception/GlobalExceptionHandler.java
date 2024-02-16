package damon.backend.exception;

import damon.backend.dto.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public Result<Void> handleException(EntityNotFoundException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public Result<Void> handleException(PermissionDeniedException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(KakaoLoginException.class)
    public Result<Void> handleException(KakaoLoginException e) {
        return Result.error(e.getMessage());
    }
}
