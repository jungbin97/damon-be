package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class TokenNotValidatedException extends CustomException {

    public TokenNotValidatedException() {

        super(400, Status.BAD_REQUEST, "잘못된 토큰 정보입니다.");
    }
}
