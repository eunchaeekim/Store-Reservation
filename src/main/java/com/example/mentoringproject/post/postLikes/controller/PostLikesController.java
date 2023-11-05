package com.example.mentoringproject.post.postLikes.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.postLikes.service.PostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/postLikes")
public class PostLikesController {

  private final PostLikesService postLikesService;

  // 좋아요 등록/취소
  @PostMapping
  public ResponseEntity<String> PostLikes(@PathVariable Long postId) {
    String email = SpringSecurityUtil.getLoginEmail();
    postLikesService.switchPostLikes(email, postId);
    return ResponseEntity.ok("Success");
  }
}
