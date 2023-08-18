package com.example.store.common.exception.impl.reservation;

import org.springframework.http.HttpStatus;

public class NotUseThisStoreException extends AbstractMethodError{
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이용하지 않은 상점에 대해 리뷰를 작성할 수 없습니다.";
    }
}
