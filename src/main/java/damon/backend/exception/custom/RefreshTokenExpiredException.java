package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class RefreshTokenExpiredException extends CustomException {

    public RefreshTokenExpiredException() {
        super(403, Status.FORBIDDEN, "리프레시 토큰의 유효 기간이 만료되었습니다.");
    }
}
