package damon.backend.exception;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
        super("수정 및 삭제 권한이 없습니다.");
    }
}