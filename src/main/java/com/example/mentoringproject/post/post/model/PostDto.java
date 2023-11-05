package com.example.mentoringproject.post.post.model;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostDto {

  private Long id;

  private Category category;
  private String title;
  private String content;

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;

  public static List<PostDto> fromEntity(Page<Post> page) {
    return page.getContent().stream()
        .map(post -> PostDto.builder()
            .id(post.getId())
            .category(post.getCategory())
            .title(post.getTitle())
            .content(post.getContent())
            .registerDatetime(post.getRegisterDatetime())
            .updateDatetime(post.getUpdateDatetime())
            .build()
        )
        .collect(Collectors.toList());
  }

  public static PostDto fromEntity(Post post) {
    return PostDto.builder()
            .id(post.getId())
            .category(post.getCategory())
            .title(post.getTitle())
            .content(post.getContent())
            .registerDatetime(post.getRegisterDatetime())
            .updateDatetime(post.getUpdateDatetime())
            .build();
  }

}
