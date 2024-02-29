package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class AccessTokenExpiredException extends CustomException {

    public AccessTokenExpiredException() {
        super(403, Status.FORBIDDEN, "엑세스 토큰의 유효 기간이 만료되었습니다.");
    }
}
