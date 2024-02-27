package damon.backend.exception;

import lombok.Getter;

@Getter
public class KakaoLoginException extends RuntimeException {

    public KakaoLoginException() {
        super("카카오 로그인에 실패했습니다.");
    }
}