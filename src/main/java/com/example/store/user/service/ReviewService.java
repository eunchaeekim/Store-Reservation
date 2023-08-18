package com.example.store.user.service;

import com.example.store.common.config.JwtTokenProvider;
import com.example.store.common.exception.impl.reservation.NotUseThisStoreException;
import com.example.store.common.exception.impl.role.UnauthorizedException;
import com.example.store.common.exception.impl.store.InvalidStoreNameException;
import com.example.store.common.exception.impl.store.NotExistStoreException;
import com.example.store.partner.domain.entity.Store;
import com.example.store.partner.domain.repository.StoreRepository;
import com.example.store.user.domain.dto.ReviewDto;
import com.example.store.user.domain.entity.Review;
import com.example.store.user.domain.repository.ReservationRepository;
import com.example.store.user.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final JwtTokenProvider jwtTokenProvider;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public void createReview(ReviewDto reviewDto, HttpServletRequest request) {
        // 토큰으로부터 사용자 핸드폰 번호 가져오기
        String token = jwtTokenProvider.resolveToken(request);
        String userPhoneNum = jwtTokenProvider.getUserPhoneNum(token);

        // 상점 ID 조회
        String storeName = reviewDto.getStoreName();
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new NotExistStoreException();
        }

        //상점 이용하지 않은 고객이 이용하려고 할 때
        // 예약한 고객인지 확인하여 음식을 먹었는지 검토
        boolean hasReservation = reservationRepository.existsByStoreIdAndUserPhoneNum(store.getStoreId(), userPhoneNum);
        if (!hasReservation) {
            throw new NotUseThisStoreException();
        }

        // 새로운 리뷰 엔티티 생성
        Review review = new Review();
        review.setUserPhoneNum(userPhoneNum);
        review.setStoreId(store.getStoreId());
        review.setStoreName(store.getStoreName());
        review.setReviewText(reviewDto.getReviewText());
        review.setStarRating(reviewDto.getStarRating());

        // 리뷰를 데이터베이스에 저장
        reviewRepository.save(review);

        // 상점의 총 별점 총점과 리뷰 개수 업데이트
        double newTotalRating = store.getTotalRating() + review.getStarRating();
        int newTotalReviewCount = store.getTotalReviewCount() + 1;

        // 평균 점수 계산
        double newAverageRating = newTotalRating / newTotalReviewCount;

        if (newAverageRating > 5.0) {
            newAverageRating = 5.0; // 평균 점수가 5를 초과하지 않도록 제한
        } else if (newAverageRating < 0.0) {
            newAverageRating = 0.0; // 평균 점수가 0보다 작아지지 않도록 제한
        }

        // 상점 엔티티에 업데이트된 평균 점수와 리뷰 개수 설정
        store.setTotalRating((long) newTotalRating);
        store.setTotalReviewCount(newTotalReviewCount);
        store.setAverageRating(newAverageRating);

        storeRepository.save(store);
    }

    //리뷰 삭제
    public void deleteReview(Long reviewId, HttpServletRequest request) {
        //토큰에서 리뷰 작성자의 id 가져옴
        String token = jwtTokenProvider.resolveToken(request);
        String userPhoneNum = jwtTokenProvider.getUserPhoneNum(token);

        //id로 리뷰를 찾음
        Review review = reviewRepository.findById(reviewId).orElse(null);

        //작성자 핸드폰 번호와 토큰에서 조회한 핸드폰 번호 일치 여부 확인
        if (!Objects.requireNonNull(review).getUserPhoneNum().equals(userPhoneNum)) {
            throw new UnauthorizedException();
        }

        //리뷰 삭제
        reviewRepository.delete(review);
    }

    //상점 이름으로 상점에 대한 모든 리뷰 조회
    public List<Review> viewReview(String storeName) {
        //상점 조회
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            // 상점이 존재하지 않을 경우 빈 리뷰 목록 반환
            throw new InvalidStoreNameException();
        }
        //리뷰 조회
        return reviewRepository.findByStoreId(store.getStoreId());
    }
}
