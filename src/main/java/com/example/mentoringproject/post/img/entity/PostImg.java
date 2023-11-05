package com.example.mentoringproject.post.img.entity;

import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.post.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostImg {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "img_id")
  private Long id;

  private String uploadPath;
  private String uploadName;
  private String uploadUrl;

  @CreatedDate
  private LocalDateTime registerDatetime;
  @LastModifiedDate
  private LocalDateTime updateDatetime;
  private LocalDateTime deleteDatetime;


  @ManyToOne
  @JoinColumn(name = "post_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Post post;

  public static Set<PostImg> from(List<S3FileDto> s3FileDtoList, Post post){
    return s3FileDtoList.stream()
        .map(s3File -> PostImg.builder()
            .post(post)
            .uploadName(s3File.getUploadName())
            .uploadPath(s3File.getUploadPath())
            .uploadUrl(s3File.getUploadUrl())
            .build())
        .collect(Collectors.toSet());
  }

}