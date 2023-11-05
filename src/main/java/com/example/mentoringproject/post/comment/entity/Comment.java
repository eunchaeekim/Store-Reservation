package com.example.mentoringproject.post.comment.entity;

import com.example.mentoringproject.post.comment.model.CommentRegisterRequest;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  private String comment;

  @CreatedDate
  private LocalDateTime registerDatetime;
  @LastModifiedDate
  private LocalDateTime updateDatetime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;


  public static Comment from (User user, Post post, CommentRegisterRequest commentRegisterRequest) {
    return Comment.builder()
        .user(user)
        .post(post)
        .comment(commentRegisterRequest.getComment())
        .build();
  }

}
