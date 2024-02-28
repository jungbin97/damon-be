package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class AccessTokenNotFoundException extends CustomException {

    public AccessTokenNotFoundException() {
        super(404, Status.NOT_FOUND, "토큰을 찾을 수 없습니다.");
    }
}
