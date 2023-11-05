package com.example.mentoringproject.ElasticSearch.mentor.model;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentorSearchDto {

  private Long id;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;

  private Double rating;

  public static MentorSearchDto fromDocument(MentorSearchDocumment mentorSearchDocumment) {
    return MentorSearchDto.builder()
        .id(mentorSearchDocumment.getId())
        .name(mentorSearchDocumment.getName())
        .career(mentorSearchDocumment.getCareer())
        .introduce(mentorSearchDocumment.getIntroduce())
        .mainCategory(mentorSearchDocumment.getMainCategory())
        .middleCategory(mentorSearchDocumment.getMiddleCategory())
        .rating(mentorSearchDocumment.getRating())
        .build();
  }

}
