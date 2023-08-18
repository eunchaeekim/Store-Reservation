package com.example.store.partner.domain.entity;

import com.example.store.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;//매장 번호
    private String userPhoneNum; //사장인지 확인하기 위한 사장 핸드폰번호(여기선 ID로 쓰임)

    @Column(unique = true)
    private String storeName; //매장 이름

    private String storeLocation; //매장 위치
    private String storeDescription; //매장 설명

    private int distance; //매장으로부터의 거리

    // (자리수, 소수점 이하 자릿수) (별점은 1~5점만 가능)
    @Column(columnDefinition = "DOUBLE(3, 1) CHECK (average_rating >= 0.0 AND average_rating <= 5.0)")
    private double averageRating;
    private Long totalRating;
    private int totalReviewCount;
}
