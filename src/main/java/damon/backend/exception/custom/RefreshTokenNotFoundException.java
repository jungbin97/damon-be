package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class RefreshTokenNotFoundException extends CustomException {

    public RefreshTokenNotFoundException() {
        super(404, Status.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다.");
    }
}
