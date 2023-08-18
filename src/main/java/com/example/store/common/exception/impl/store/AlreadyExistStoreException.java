package com.example.store.common.exception.impl.store;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistStoreException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 상점 이름입니다.";
    }
}