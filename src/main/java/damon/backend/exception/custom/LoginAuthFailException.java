package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class LoginAuthFailException extends CustomException {

    public LoginAuthFailException() {
        super(400, Status.BAD_REQUEST, "로그인 인증에 실패했습니다.");
    }
}
