package com.example.mentoringproject.user.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringList;
import com.example.mentoringproject.user.model.UserJoinDto;
import com.example.mentoringproject.user.model.UserProfile;
import com.example.mentoringproject.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/test")
  public String authTest() {
    return "test-success";
  }

  @PostMapping("/join/email/auth")
  public ResponseEntity<String> sendEmailAuth(@RequestParam("email") String email) {
    userService.sendEmailAuth(email);
    return ResponseEntity.ok("email send success");
  }

  @PostMapping("/join/email/auth/verify")
  public ResponseEntity<String> verifyEmailAuth(@RequestParam("email") String email, @RequestParam("authCode") String authCode) {
    userService.verifyEmailAuth(email, authCode);
    return ResponseEntity.ok("email auth verify success");
  }

  @PostMapping("/join/email/nickname/verify")
  public ResponseEntity<String> checkDuplicateNickname(@RequestParam("nickName") String nickName) {
    userService.checkDuplicateNickName(nickName);
    return ResponseEntity.ok("check nickname success");
  }

  @PostMapping("/join/email")
  public ResponseEntity<String> joinEmailUser(@RequestBody UserJoinDto parameter) {
    userService.joinEmailUser(parameter);
    return ResponseEntity.ok("email join success");
  }

  @GetMapping("/join/success")
  public ResponseEntity<?> joinSuccessPage() {
    return ResponseEntity.ok().build();
  }

  @PostMapping("/profile")
  public ResponseEntity<UserProfile> createProfile(
      @RequestPart UserProfile userProfile,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFile

  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.createProfile(email, userProfile, multipartFile)));
  }
  @PutMapping("/profile")
  public ResponseEntity<UserProfile> updateProfile(
      @RequestPart UserProfile userProfile,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFile
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.updateProfile(email, userProfile, multipartFile)));
  }
  @GetMapping("/profile/{userId}")
  public ResponseEntity<UserProfile>  profileInfo(
      @PathVariable Long userId
  ) {
    return ResponseEntity.ok(UserProfile.from(userService.profileInfo(userId)));
  }

  @GetMapping("/profile")
  public ResponseEntity<Page<UserProfile>> getProfileList(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);

    return ResponseEntity.ok(UserProfile.from(userService.getProfileList(pageable)));
  }
}