package com.example.store.partner.domain.repository;

import com.example.store.partner.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByStoreName(String storeName);
    boolean existsByStoreName(String storeName);

    Store findIdByStoreName(String storeName);

    List<Store> findByUserPhoneNum(String userPhoneNum);

    Optional<Store> findByStoreId(Long storeId);

    Long findStoreIdByStoreName(String storeName);
}
