package com.example.mentoringproject.post.postLikes.entity;

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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "postlikes")
@EntityListeners(AuditingEntityListener.class)
public class PostLikes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "postlikes_id")
  private Long id;

  @CreatedDate
  private LocalDateTime registerDatetime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;


  public static PostLikes from(User user, Post post) {
    return PostLikes.builder()
        .user(user)
        .post(post)
        .build();
  }
}
