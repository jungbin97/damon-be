package damon.backend.exception.custom;

import damon.backend.exception.CustomException;
import damon.backend.exception.Status;

public class ImageUploadFailedException extends CustomException {
    public ImageUploadFailedException() {
        super(500, Status.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
    }
}