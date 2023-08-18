package com.example.store.common.exception.impl.token;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistTokenException extends AbstractException{
        @Override
        public int getStatusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String getMessage() {
            return "토큰에 관한 정보를 확인하십시오.";
        }
}
