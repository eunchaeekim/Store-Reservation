package com.example.store.common.exception.impl.reservation;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistReservationException extends AbstractException{
        @Override
        public int getStatusCode() {
            return HttpStatus.BAD_REQUEST.value();
        }

        @Override
        public String getMessage() {
            return "존재하지 않는 예약입니다.";
        }
}
