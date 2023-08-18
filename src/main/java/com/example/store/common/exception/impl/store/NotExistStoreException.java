package com.example.store.common.exception.impl.store;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistStoreException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 상점입니다.";
    }
}