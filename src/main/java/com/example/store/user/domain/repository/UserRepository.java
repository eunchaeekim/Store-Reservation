package com.example.store.user.domain.repository;

import com.example.store.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserPhoneNum(String userPhoneNum);

    boolean existsByUserPhoneNum(String userPhoneNum);
}
