package com.example.store.partner.controller;

import com.example.store.partner.domain.dto.StoreDto;
import com.example.store.partner.domain.entity.Store;
import com.example.store.partner.domain.repository.StoreRepository;
import com.example.store.partner.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "상점 컨트롤러")
@RestController
@RequestMapping("/shop/v1/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;

    // 상점 등록
    @ApiOperation(value = "상점 등록", notes = "파트너 권한을 가진 유저가 상점을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerStore(@RequestBody StoreDto storeDto, HttpServletRequest request) {
        boolean success = storeService.registerStore(storeDto, request);

        if (success) {
            return ResponseEntity.ok("Store registered successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN users can register a store.");
        }
    }

    //상점 삭제
    @ApiOperation(value = "상점 삭제", notes = "파트너 권한을 가진 유저가 상점을 삭제합니다.")
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId, HttpServletRequest request) {
        storeService.deleteStore(storeId, request);
        return ResponseEntity.ok("deleteStore successfully.");
    }

    // 상점 수정
    @ApiOperation(value = "상점 수정", notes = "파트너 권한을 가진 유저가 상점 정보를 수정합니다.")
    @PutMapping("/update/{storeId}")
    public ResponseEntity<String> updateStore(@PathVariable Long storeId, @RequestBody StoreDto storeDto, HttpServletRequest request) {
        storeService.updateStore(storeId, storeDto, request);
        return ResponseEntity.ok("Store updated successfully.");
    }

    //상점 개별 조회
    @ApiOperation(value = "상점 조회", notes = "상점의 이름을 통해 상점을 조회합니다.")
    @GetMapping("/view/{storeName}")
    public ResponseEntity<Store> getStore(@PathVariable String storeName) {
        Store store = storeService.viewStore(storeName);
        return ResponseEntity.ok(store);
    }

    // 모든 매장 정보 확인
    // 아래처럼 작성해도 되지만 필요한 컬럼만 가져오기 위해 따로 service 가져옴
    // List<Store> stores = storeRepository.findAll();
    @ApiOperation(value = "상점 전체 조회", notes = "모든 매장의 목록을 확인합니다.")
    @GetMapping("/view")
    public List<Map<String, Object>> getAllStores() {
        return storeService.getAllStores();
    }


    @ApiOperation(value = "상점 가나다순 조회", notes = "모든 매장의 목록을 가나다순으로 확인합니다.")
    @GetMapping("/view/name")
    public ResponseEntity<List<Store>> getStoresByName() {
        List<Store> stores = storeRepository.findAll(Sort.by(Sort.Direction.ASC, "storeName"));
        return ResponseEntity.ok(stores);
    }

    @ApiOperation(value = "상점 별점순 조회", notes = "모든 매장의 목록을 별점순으로 확인합니다.")
    @GetMapping("/view/rating")
    public ResponseEntity<List<Store>> getStoresByRating() {
        List<Store> stores = storeRepository.findAll(Sort.by(Sort.Direction.DESC, "averageRating"));
        return ResponseEntity.ok(stores);
    }

    @ApiOperation(value = "상점 거리순 조회", notes = "모든 매장의 목록을 거리순으로 확인합니다.")
    @GetMapping("/view/distance")
    public ResponseEntity<List<Store>> getStoresByDistance() {
        List<Store> stores = storeRepository.findAll(Sort.by(Sort.Direction.ASC, "distance"));
        return ResponseEntity.ok(stores);
    }
}
