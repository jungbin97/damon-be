package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class KakaoAuthFailException extends CustomException {

    public KakaoAuthFailException() {
        super(400, Status.BAD_REQUEST, "카카오 인증에 실패했습니다.");
    }
}
