package com.example.store.user.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    //유저 아이디는 토큰을 통해서 가져오면 됨. 생성시간은 BaseEntity로 인해 자동생성 됨
    private String storeName; //상점 이름
    private String reviewText; //리뷰 텍스트
    private int starRating; //별점
}
