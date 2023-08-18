package com.example.store.common.exception.impl.user;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistUserException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 사용자입니다.";
    }
}