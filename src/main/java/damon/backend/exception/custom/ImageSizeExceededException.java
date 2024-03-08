package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class ImageSizeExceededException extends CustomException {
    public ImageSizeExceededException() {
        super(413, Status.PAYLOAD_TOO_LARGE, "허용된 이미지 크기를 초과했습니다.");
    }
}