package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
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
public class MentoringByEndDateDto {

  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;
  private String uploadUrl;

  public static List<MentoringByEndDateDto> fromEntity(List<Mentoring> mentoringList) {
    return mentoringList.stream()
        .map(mentoring -> MentoringByEndDateDto.builder()
            .title(mentoring.getTitle())
            .content(mentoring.getContent())
            .startDate(mentoring.getStartDate())
            .endDate(mentoring.getEndDate())
            .numberOfPeople(mentoring.getNumberOfPeople())
            .amount(mentoring.getAmount())
            .status(mentoring.getStatus())
            .category(mentoring.getCategory())
            .uploadUrl(mentoring.getUploadUrl())
            .build())
        .collect(Collectors.toList());
  }

}
