package com.example.store.common.exception.impl.reservation;

import com.example.store.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "예약이 전혀 존재하지 않습니다.";
    }
}
