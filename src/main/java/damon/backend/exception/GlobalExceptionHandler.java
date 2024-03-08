package damon.backend.exception;

import damon.backend.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외를 처리하기 위한 클래스입니다.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result<Void>> handleCustomException(CustomException e) {
        log.error("ErrorMessage={}", e.getErrorMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(e.getCode()).body(Result.error(e));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleParameterTypeException(MethodArgumentTypeMismatchException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(400).body(Result.error(Status.BAD_REQUEST, "잘못된 요청 파라미터 값입니다."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingParameterException(MissingServletRequestParameterException e) {
        log.error("ErrorMessage={}", e.getMessage());
        log.error("StackTrace={}", ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(400).body(Result.error(Status.BAD_REQUEST, "요청 파라미터가 누락되었습니다."));
    }
}

