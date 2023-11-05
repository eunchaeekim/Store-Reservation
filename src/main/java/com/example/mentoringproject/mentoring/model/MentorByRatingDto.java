package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentorByRatingDto {
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String imgUrl;

  private Double rating; // 평점

  public static List<MentorByRatingDto> fromEntity(List<User> userList) {
    return userList.stream()
        .map(user -> MentorByRatingDto.builder()
            .name(user.getName())
            .career(user.getCareer())
            .introduce(user.getIntroduce())
            .mainCategory(user.getMainCategory())
            .middleCategory(user.getMiddleCategory())
            .imgUrl(user.getUploadUrl())
            .build())
        .collect(Collectors.toList());
  }


}
