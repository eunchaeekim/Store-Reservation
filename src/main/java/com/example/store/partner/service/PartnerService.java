package com.example.store.partner.service;

import com.example.store.common.config.JwtTokenProvider;
import com.example.store.common.exception.impl.reservation.NoReservationException;
import com.example.store.common.exception.impl.user.NotExistUserException;
import com.example.store.partner.domain.entity.Store;
import com.example.store.partner.domain.repository.StoreRepository;
import com.example.store.type.ReservationStatus;
import com.example.store.user.domain.entity.Reservation;
import com.example.store.user.domain.entity.User;
import com.example.store.user.domain.repository.ReservationRepository;
import com.example.store.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    //권한 변경
    public void changeRole(String userPhoneNum) {
        User member = userRepository.findByUserPhoneNum(userPhoneNum)
                .orElseThrow(NotExistUserException::new);

        member.setRoles(Collections.singletonList("ADMIN")); // roles를 "ADMIN"으로 변경

        userRepository.save(member);
    }

    // 파트너의 전화번호를 통해 전체 예약 목록을 조회
    public List<Reservation> getReservationsByPartnerPhoneNum(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String storeOwnerUserPhoneNum = jwtTokenProvider.getUserPhoneNum(token);
        // 상점 주인의 userPhoneNum을 통해 상점들을 조회
        List<Store> stores = storeRepository.findByUserPhoneNum(storeOwnerUserPhoneNum);

        // 상점들의 storeId 목록을 추출
        List<Long> storeIds = stores.stream()
                .map(Store::getStoreId)
                .collect(Collectors.toList());

        if (storeIds.isEmpty()) throw new NoReservationException();


        // 상점들의 storeId 목록을 통해 예약 목록을 시간 빠른 순서로
        return reservationRepository.findByStoreIdInOrderByReservationTimeAsc(storeIds);
    }

    // 파트너의 전화번호를 통해 지금시간 이후의 예약 목록을 조회
    public List<Reservation> getReservationsByPartnerPhoneNumAfterNow(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String storeOwnerUserPhoneNum = jwtTokenProvider.getUserPhoneNum(token);

        // 상점 주인의 userPhoneNum을 통해 상점들을 조회
        List<Store> stores = storeRepository.findByUserPhoneNum(storeOwnerUserPhoneNum);

        // 상점들의 storeId 목록을 추출
        List<Long> storeIds = stores.stream()
                .map(Store::getStoreId)
                .collect(Collectors.toList());

        if (storeIds.isEmpty()) throw new NoReservationException();

        // 현재 날짜와 시간을 가져옴
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 상점들의 storeId 목록을 통해 예약 목록을 조회
        List<Reservation> reservations = reservationRepository.findByStoreIdIn(storeIds);

        // 현재 날짜와 비교하여 이미 지난 예약을 필터링
        List<Reservation> filteredReservations = reservations.stream()
                .filter(reservation -> reservation.getReservationTime().isAfter(currentDateTime))
                .collect(Collectors.toList());

        return filteredReservations;
    }


    // 토큰에서 전화번호 추출
    public String extractUserPhoneNumFromToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.getUserPhoneNum(token);
    }

    // 예약 상태 업데이트
    public boolean updateReservationStatus(Long reservationId, HttpServletRequest request, ReservationStatus status) {
        // 토큰을 통해 상점 주인의 userPhoneNum 가져오기
        String token = jwtTokenProvider.resolveToken(request);
        String storeOwnerUserPhoneNum = jwtTokenProvider.getUserPhoneNum(token);

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            Optional<Store> optionalStore = storeRepository.findByStoreId(reservation.getStoreId());
            if (optionalStore.isPresent() && optionalStore.get().getUserPhoneNum().equals(storeOwnerUserPhoneNum)) {
                // 예약 상태 업데이트 로직
                reservation.setStatus(status);
                reservationRepository.save(reservation);
                return true;
            }
        }
        return false;
    }
}
