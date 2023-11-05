package com.example.mentoringproject.login.email.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities("USER")
        .build();
  }

  @Transactional
  public void setLastLogin(User oAuth2User) {
    User user = userRepository.findByEmail(oAuth2User.getEmail())
        .orElseThrow(() -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Not Found OAuth2User"));
    user.setLastLogin(LocalDateTime.now());
  }
}