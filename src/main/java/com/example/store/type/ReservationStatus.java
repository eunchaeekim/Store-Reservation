package com.example.store.type;

//사장 승인을 통한 예약완료
public enum ReservationStatus {
    REFUSE,
    PLEASE_WAIT,
    OKAY;

    public static final ReservationStatus DEFAULT = PLEASE_WAIT;
}
