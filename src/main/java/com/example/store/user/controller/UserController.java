package com.example.store.user.controller;

import com.example.store.user.domain.dto.UserCreateDto;
import com.example.store.user.domain.dto.UserDeleteDto;
import com.example.store.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "회원 컨트롤러")
@RestController
@RequestMapping("/shop/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "회원 가입", notes = "아이디(휴대폰 번호), 비밀번호를 통해 회원가입 요청을 보냅니다.")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserCreateDto userCreateDto) {
        userService.join(userCreateDto);
        return ResponseEntity.ok("join successfully");
    }

    @ApiOperation(value = "회원 탈퇴", notes = "아이디(휴대폰 번호), 비밀번호를 통해 회원탈퇴 요청을 보냅니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserDeleteDto userDeleteDto) {
        userService.deleteUser(userDeleteDto.getUserPhoneNum(), userDeleteDto.getUserPassword());
        return ResponseEntity.ok("delete successfully");
    }

    @ApiOperation(value = "로그인", notes = "jwt 토큰을 사용하여 로그인시 토큰을 부여합니다.")
    @PostMapping("/login")
    public String login(@RequestBody UserCreateDto userCreateDto) {
        return "Bearer " + userService.login(userCreateDto);
    }
}
