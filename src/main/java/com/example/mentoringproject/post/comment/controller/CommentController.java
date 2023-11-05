package com.example.mentoringproject.post.comment.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.comment.model.CommentDto;
import com.example.mentoringproject.post.comment.model.CommentRegisterRequest;
import com.example.mentoringproject.post.comment.model.CommentUpdateRequest;
import com.example.mentoringproject.post.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  // 댓글 등록
  @PostMapping
  public ResponseEntity<CommentDto> createComment(@PathVariable Long postId,
      @RequestBody CommentRegisterRequest commentRegisterRequest) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(
        CommentDto.fromEntity(commentService.createComment(email, postId, commentRegisterRequest)));
  }

  // 댓글 수정
  @PutMapping("/{commentId}")
  public ResponseEntity<CommentDto> updatePost(@PathVariable Long postId,
      @PathVariable Long commentId,
      @RequestBody CommentUpdateRequest commentUpdateRequest) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(CommentDto.fromEntity(
        commentService.updateComment(email, postId, commentId, commentUpdateRequest)));
  }

  // 글 삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
      @PathVariable Long commentId) {
    String email = SpringSecurityUtil.getLoginEmail();
    commentService.deleteComment(email, postId, commentId);
    return ResponseEntity.ok().build();
  }

  // 전체 목록 조회
  @GetMapping
  public ResponseEntity<Page<CommentDto>> getAllComments(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.fromString(sortDirection),
        sortBy);

    return ResponseEntity.ok(commentService.findAllComments(pageable).map(CommentDto::fromEntity));
  }

}
