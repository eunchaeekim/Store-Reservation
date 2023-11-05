package com.example.mentoringproject.post.post.model;

import com.example.mentoringproject.post.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostRegisterRequest {
  private Category category;
  private String title;
  private String content;

}
