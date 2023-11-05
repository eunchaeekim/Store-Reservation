package com.example.mentoringproject.user.repository;

import com.example.mentoringproject.user.entity.SocialType;
import com.example.mentoringproject.user.entity.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByRefreshToken(String refreshToken);

  boolean existsByEmail(String email);

  Optional<User> findByEmailAndEmailAuth(String email, String authCode);

  void deleteByEmail(String email);

  boolean existsByIdAndNameIsNotNull(Long id);

  List<User> findTop50ByOrderByRatingDesc();

  Optional<User> findByNickNameAndRegisterDateIsNotNull(String nickname);

  Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

  Page<User> findByNameIsNotNull(Pageable pageable);

}
