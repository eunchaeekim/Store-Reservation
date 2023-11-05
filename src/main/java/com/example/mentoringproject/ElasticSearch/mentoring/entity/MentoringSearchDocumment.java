package com.example.mentoringproject.ElasticSearch.mentoring.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "mentoring")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentoringSearchDocumment {

  @Id
  private Long id;
  private String writer;
  private String title;
  private String content;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;
  private String imgUrl;

  private Double rating;
  private Long Cost;

  public static MentoringSearchDocumment fromEntity(User user, Mentoring mentoring) {
    return MentoringSearchDocumment.builder()
        .id(mentoring.getId())
        .writer(user.getEmail())
        .title(mentoring.getTitle())
        .content(mentoring.getCategory())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .rating(user.getRating())
        .build();
  }
}