package com.example.store.common.exception.impl.role;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "옳지 않은 권한을 통한 접근입니다.";
    }
}
