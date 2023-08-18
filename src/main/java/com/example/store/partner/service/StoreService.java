package com.example.store.partner.service;

import com.example.store.common.config.JwtTokenProvider;
import com.example.store.common.exception.impl.role.UnauthorizedException;
import com.example.store.common.exception.impl.store.AlreadyExistStoreException;
import com.example.store.common.exception.impl.store.InvalidStoreNameException;
import com.example.store.common.exception.impl.store.NotExistStoreException;
import com.example.store.partner.domain.dto.StoreDto;
import com.example.store.partner.domain.entity.Store;
import com.example.store.partner.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //상점 등록
    public boolean registerStore(StoreDto storeDto, HttpServletRequest request) {
        // 토큰을 통해 사용자 정보를 가져옴
        String token = jwtTokenProvider.resolveToken(request);

        // 토큰에서 전화번호 추출
        String userPhoneNum = jwtTokenProvider.getUserPhoneNum(token);

        if (jwtTokenProvider.userHasAdminRole(token)) { // 권한이 있어야 store 등록 가능
            //상점 이름 중복 확인
            if (storeRepository.existsByStoreName(storeDto.getStoreName())) {
                throw new AlreadyExistStoreException();
            }
            Store store = new Store();
            store.setStoreName(storeDto.getStoreName());
            store.setStoreLocation(storeDto.getStoreLocation());
            store.setStoreDescription(storeDto.getStoreDescription());
            store.setUserPhoneNum(userPhoneNum);
            store.setDistance(storeDto.getDistance());
            store.setAverageRating(0.0);
            store.setTotalReviewCount(0);
            storeRepository.save(store);
            return true;
        } else {
            throw new UnauthorizedException();
        }
    }


    //상점 삭제
    public boolean deleteStore(Long storeId, HttpServletRequest request) {
        // 토큰을 통해 사용자 정보를 가져옴
        String token = jwtTokenProvider.resolveToken(request);
        String userPhoneNum = jwtTokenProvider.getUserPhoneNum(token);
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        String storeUserPhoneNum = store.getUserPhoneNum();
        // 권한이 ADMIN인지 확인(=PARTNER 등록이 되어있는지) && userPhoneNum 과 storeUserPhoneNum이 같은지
        if (jwtTokenProvider.userHasAdminRole(token) && userPhoneNum.equals(storeUserPhoneNum)) { // 권한이 있어야 store 삭제 가능
            storeRepository.deleteById(storeId);
            return true;
        } else {
            throw new UnauthorizedException();
        }
    }

    //상점 수정
    public boolean updateStore(Long storeId, StoreDto storeDto, HttpServletRequest request) {
        // 토큰을 통해 사용자 정보를 가져옴
        String token = jwtTokenProvider.resolveToken(request);
        String userPhoneNum = jwtTokenProvider.getUserPhoneNum(token);

        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            String storeUserPhoneNum = store.getUserPhoneNum();

            // 권한이 ADMIN인지 확인(=PARTNER 등록이 되어있는지) && userPhoneNum 과 storeUserPhoneNum이 같은지
            if (jwtTokenProvider.userHasAdminRole(token) && userPhoneNum.equals(storeUserPhoneNum)) { // 권한이 있어야 store 수정 가능
                store.setStoreName(storeDto.getStoreName());
                store.setStoreDescription(storeDto.getStoreDescription());
                store.setStoreLocation(storeDto.getStoreLocation());
                store.setDistance(storeDto.getDistance());
                store.setAverageRating(store.getAverageRating());
                store.setTotalReviewCount(0);
                store.setTotalRating(0L);
                store.setTotalRating(0L);
                storeRepository.save(store);
                return true;
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new NotExistStoreException();
        }
    }


    //상점 이름으로 조회
    public Store viewStore(String storeName) {
        if (storeName == null || storeName.isEmpty()) {
            throw new InvalidStoreNameException();
        }
        return storeRepository.findByStoreName(storeName);
    }


    //상점 조회
    public List<Map<String, Object>> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Store store : stores) { // 원하는 정보만 가져올 수 있도록 (필요없는 등록일자, 수정일자등은 삭제)
            Map<String, Object> storeInfo = new HashMap<>();
            storeInfo.put("storeId", store.getStoreId());
            storeInfo.put("storeName", store.getStoreName());
            storeInfo.put("storeLocation", store.getStoreLocation());
            result.add(storeInfo);
        }

        return result;
    }
}
