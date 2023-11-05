package com.example.mentoringproject.post.comment.service;


import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.comment.model.CommentRegisterRequest;
import com.example.mentoringproject.post.comment.model.CommentUpdateRequest;
import com.example.mentoringproject.post.comment.repository.CommentRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  // 댓글 등록
  public Comment createComment(String email, Long postId, CommentRegisterRequest commentRegisterRequest) {
    User user = getUser(email);

    Post post = postRepository.findById(postId)
        .orElseThrow(() ->  new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    Comment comment = commentRepository.save(Comment.from(user, post, commentRegisterRequest));

    return comment;
  }

  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User"));
  }

  // 댓글 수정
  @Transactional
  public Comment updateComment(String email, Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Comment"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    comment.setComment(commentUpdateRequest.getComment());
    comment.setUpdateDatetime(LocalDateTime.now());

    commentRepository.save(comment);

    return comment;

  }

  // 댓글 삭제
  @Transactional
  public void deleteComment(String email, Long postId, Long commentId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Comment"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    commentRepository.deleteById(commentId);
  }

  // 모든 댓글 조회
  @Transactional(readOnly = true)
  public Page<Comment> findAllComments(Pageable pageable) {

    return commentRepository.findAll(pageable);
  }
}
