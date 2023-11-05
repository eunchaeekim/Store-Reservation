package com.example.mentoringproject.ElasticSearch.mentoring.model;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentoringSearchDto {

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

  public static MentoringSearchDto fromDocument(
      MentoringSearchDocumment mentoringSearchDocumment) {
    return MentoringSearchDto.builder()
        .id(mentoringSearchDocumment.getId())
        .writer(mentoringSearchDocumment.getWriter())
        .title(mentoringSearchDocumment.getTitle())
        .content(mentoringSearchDocumment.getContent())
        .numberOfPeople(mentoringSearchDocumment.getNumberOfPeople())
        .amount(mentoringSearchDocumment.getAmount())
        .status(mentoringSearchDocumment.getStatus())
        .category(mentoringSearchDocumment.getCategory())
        .rating(mentoringSearchDocumment.getRating())
        .build();
  }
}