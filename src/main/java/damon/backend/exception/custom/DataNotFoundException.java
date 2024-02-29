package damon.backend.exception.custom;

import damon.backend.exception.Status;
import damon.backend.exception.CustomException;

public class DataNotFoundException extends CustomException  {

    public DataNotFoundException() {
        super(404, Status.NOT_FOUND, "데이터를 찾을 수 없습니다.");
    }
}
