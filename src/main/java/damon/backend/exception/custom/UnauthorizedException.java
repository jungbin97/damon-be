package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException() {
        super(401, Status.UNAUTHORIZED, "권한이 없습니다.");
    }
}
