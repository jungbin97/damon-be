package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(404, Status.NOT_FOUND, "존재하지 않는 사용자입니다.");
    }
}