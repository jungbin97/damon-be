package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class ReviewNotFoundException extends CustomException {
    public ReviewNotFoundException() {
        super(404, Status.NOT_FOUND, "존재하지 않는 리뷰입니다.");
    }
}
