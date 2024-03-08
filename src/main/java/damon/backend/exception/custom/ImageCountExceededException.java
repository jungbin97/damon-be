package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class ImageCountExceededException extends CustomException {
    public ImageCountExceededException() {
        super(400, Status.BAD_REQUEST, "허용된 이미지 개수를 초과했습니다.");
    }
}
