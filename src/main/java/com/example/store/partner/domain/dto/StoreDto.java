package com.example.store.partner.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {
    private String storeName; //매장 이름
    private String storeLocation; //매장 위치
    private String storeDescription; //매장 설명
    private int distance; //매장으로부터의 거리
}
