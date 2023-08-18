package com.example.store.common.exception.impl.store;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class InvalidStoreNameException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "상점 이름이 정확하지 않습니다.";
    }
}
