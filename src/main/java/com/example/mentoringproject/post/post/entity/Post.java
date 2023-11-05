package com.example.mentoringproject.post.post.entity;

import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.img.entity.PostImg;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.example.mentoringproject.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Entity(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  private Category category;
  private String title;
  private String content;

  private int postLikesCount;

  @CreatedDate
  private LocalDateTime registerDatetime;
  @LastModifiedDate
  private LocalDateTime updateDatetime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<Comment> comments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<PostLikes> postLikes = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
  List<PostImg> imgs = new ArrayList<>();

  public static Post from (User user, PostRegisterRequest postRegisterRequest) {
    return Post.builder()
        .user(user)
        .category(postRegisterRequest.getCategory())
        .title(postRegisterRequest.getTitle())
        .content(postRegisterRequest.getContent())
        .postLikesCount(0)
        .build();
  }

}
