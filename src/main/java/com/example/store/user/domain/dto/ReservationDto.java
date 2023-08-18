package com.example.store.user.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long reservationId; //예약 id

    private Long userId; //예약요청한 회원의 id
    private Long storeId; //상점 id
    private LocalDateTime reservationTime; //예약 날짜 및 시간
    private boolean isConfirmed; //예약 승인 여부
}
