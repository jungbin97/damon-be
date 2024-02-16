package damon.backend.exception;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
        super("권한이 없습니다.");
    }

    public PermissionDeniedException(String message) {
        super(message);
    }
}