package damon.backend.exception;

import damon.backend.dto.Result;
import damon.backend.util.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Result<Void>> handleException(EntityNotFoundException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<Result<Void>> handleException(PermissionDeniedException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(KakaoLoginException.class)
    public ResponseEntity<Result<Void>> handleException(KakaoLoginException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Result<Void>> handleException(TokenExpiredException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Result<Void>> handleException(TokenNotFoundException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(TokenNotValidateException.class)
    public ResponseEntity<Result<Void>> handleException(TokenNotValidateException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<Object> handleReviewException(ReviewException ex) {
        // 에러 타입에 따라 적절한 HTTP 상태 코드와 메시지 설정
        HttpStatus status = switch (ex.getErrorType()) {
            case MEMBER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case REVIEW_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case COMMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case IMAGE_UPLOAD_FAILED -> HttpStatus.INTERNAL_SERVER_ERROR;
            case IMAGE_SIZE_EXCEEDED -> HttpStatus.PAYLOAD_TOO_LARGE;
            default -> HttpStatus.BAD_REQUEST;
        };
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, status);
    }
}

