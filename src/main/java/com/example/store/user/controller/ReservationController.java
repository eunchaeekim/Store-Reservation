package com.example.store.user.controller;

import com.example.store.user.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Api(tags = "예약 컨트롤러")
@RestController
@RequestMapping("/shop/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 예약 생성
    @ApiOperation(value = "예약 생성", notes = "상점 이름과 예약 시간을 설정하여 예약을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<String> createReservation(
            @RequestParam("storeName") String storeName,
            @RequestParam("reservationTime") @DateTimeFormat(pattern = "yy-MM-dd'T'HH:mm") LocalDateTime reservationTime,
            HttpServletRequest request) {
        reservationService.createReservation(storeName, reservationTime, request);
        return ResponseEntity.ok("createReservation successfully.");
    }

    // 예약 취소
    @ApiOperation(value = "예약 취소", notes = "토큰 속 고객의 휴대폰 번호와 상점 이름을 통해 예약 취소 요청을 합니다.")
    @DeleteMapping("/cancel/{storeName}")
    public ResponseEntity<String> cancelReservation(
            @PathVariable String storeName,
            HttpServletRequest request) {
        reservationService.cancelReservation(storeName, request);
        return ResponseEntity.ok("cancelReservation successfully.");
    }

    // 예약 체크
    @ApiOperation(value = "10분전 예약 확인", notes = "예약시간 10분전에 키오스크를 통해 가게에 도착했다는 인증을 합니다.")
    @GetMapping("/check")
    public ResponseEntity<String> checkReservation(@RequestParam("userPhoneNum") String userPhoneNum) {
        reservationService.checkReservation(userPhoneNum);
        return ResponseEntity.ok("checkReservation successfully.");
    }
}
