package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class InvalidFormException extends CustomException {

    public InvalidFormException() {
        super(400, Status.BAD_REQUEST, "잘못된 입력 폼입니다.");
    }
}
