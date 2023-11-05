package com.example.mentoringproject.ElasticSearch.post.entity;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostSearchDocumment {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String title;

  @Field(type = FieldType.Text)
  private String content;

  @Field(type = FieldType.Keyword)
  private String writer;

  @Field(type = FieldType.Keyword)
  private Category category;

  @Field(type = FieldType.Long)
  private int postLikesCounty;

  public static PostSearchDocumment fromEntity(Post post) {
    return PostSearchDocumment.builder()
        .id(post.getId())
        .writer(post.getUser().getEmail())
        .category(post.getCategory())
        .title(post.getTitle())
        .content(post.getContent())
        .postLikesCounty(post.getPostLikesCount())
        .build();
  }
}
