package com.example.store.user.service;

import com.example.store.common.config.JwtTokenProvider;
import com.example.store.common.exception.impl.user.AlreadyExistUserException;
import com.example.store.common.exception.impl.user.NotExistUserException;
import com.example.store.common.exception.impl.user.PasswordNotMatchException;
import com.example.store.user.domain.dto.UserCreateDto;
import com.example.store.user.domain.entity.User;
import com.example.store.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    public Long join(UserCreateDto userCreateDto) {
        String userPhoneNum = userCreateDto.getUserPhoneNum();

        // 이미 존재하는 사용자인지 확인
        if (userRepository.existsByUserPhoneNum(userPhoneNum)) {
            throw new AlreadyExistUserException();
        }

        User user = User.builder()
                .userPhoneNum(userPhoneNum)
                .userPassword(passwordEncoder.encode(userCreateDto.getUserPassword())) // passwordEncoder 내장함수,  사용자 비밀번호를 평문으로 저장하지 않고, 보안 강화를 위해 해시 함수를 이용하여 저장
                .roles(Collections.singletonList("USER")) // collections 내장함수
                .build();

        return userRepository.save(user).getUserId();
    }

    //로그인
    // 사용자의 전화번호와 비밀번호를 확인하여 로그인을 처리하고, 로그인이 성공하면 해당 사용자에 대한 JWT 토큰을 생성하여 반환
    public String login(UserCreateDto userCreateDto) {
        // userRepository에서 사용자의 전화번호를 기반으로 검색하고, 존재하지 않으면 NotExistUserException을 발생시킵니다.
        User user = userRepository.findByUserPhoneNum(userCreateDto.getUserPhoneNum())
                .orElseThrow(NotExistUserException::new);

        // 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교합니다.
        if (!passwordEncoder.matches(userCreateDto.getUserPassword(), user.getUserPassword())) {
            // 비밀번호가 일치하지 않으면 PasswordNotMatchException을 발생시킵니다.
            throw new PasswordNotMatchException();
        }

        // 사용자의 전화번호와 역할 정보를 활용하여 JWT 토큰을 생성하고 반환합니다.
        return jwtTokenProvider.createToken(user.getUserPhoneNum(), user.getRoles());
    }


    //회원 삭제
    public boolean deleteUser(String userPhoneNum, String password) {
        Optional<User> optionalUser = userRepository.findByUserPhoneNum(userPhoneNum);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getUserPassword())) {
                userRepository.delete(user);
                return true;
            } else {
                throw new PasswordNotMatchException();
            }
        } else {
            throw new NotExistUserException();
        }
    }

}
