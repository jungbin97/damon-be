package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class CommentNotFoundException extends CustomException {
    public CommentNotFoundException() {
        super(404, Status.NOT_FOUND, "존재하지 않는 댓글입니다.");
    }
}