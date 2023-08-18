package com.example.store.partner.controller;

import com.example.store.partner.service.PartnerService;
import com.example.store.type.ReservationStatus;
import com.example.store.user.domain.entity.Reservation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "파트너 컨트롤러")
@RestController
@RequestMapping("/shop/v1/partner")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    @ApiOperation(value = "파트너 권한 부여", notes = "유저의 권한을 파트너로 변경시켜 상점 등록 및 여러 권한을 갖게 합니다.")
    @PostMapping("/changeRole")
    public ResponseEntity<String> changeRole(@RequestParam String userPhoneNum) {
        partnerService.changeRole(userPhoneNum);
        return ResponseEntity.ok("changeRole successfully.");
    }

    @ApiOperation(value = "전체 예약 목록 조회", notes = "점주의 휴대폰 번호와 일치하는 정보를 가진 가게에 대한 전체 예약 목록을 조회합니다.")
    @GetMapping("/reservations/all")
    public ResponseEntity<List<Reservation>> viewAllReservationList(HttpServletRequest request) {
        List<Reservation> reservations = partnerService.getReservationsByPartnerPhoneNum(request);
        return ResponseEntity.ok(reservations);
    }

    @ApiOperation(value = "현재시간 이후의 예약 목록 조회", notes = "점주의 휴대폰 번호와 일치하는 정보를 가진 가게에 대한 현재시간 이후의 예약 목록을 조회합니다.")
    @GetMapping("/reservations/after")
    public ResponseEntity<List<Reservation>> viewReservationList(HttpServletRequest request) {
        List<Reservation> reservations = partnerService.getReservationsByPartnerPhoneNumAfterNow(request);
        return ResponseEntity.ok(reservations);
    }

    @ApiOperation(value = "예약 승인 혹은 거절", notes = "예약이 들어왔을 때 점주가 예약 대기 상태를 예약 승인 혹은 거절 상태로 지정합니다.")
    @PostMapping("/status")
    public ResponseEntity<String> setStatus(
            @RequestParam("reservationId") Long reservationId,
            @RequestParam("status") ReservationStatus status,
            HttpServletRequest request) {

        // 예약 상태 업데이트
        boolean updated = partnerService.updateReservationStatus(reservationId, request, status);
        if (updated) {
            return ResponseEntity.ok("Reservation status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update the reservation status.");
        }
    }
}