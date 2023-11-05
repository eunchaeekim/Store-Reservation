package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.mentoringproject.mentoring.img.entity.MentoringImg;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MentoringInfo {
  private long mentoringId;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;
  private int countWatch;
  private Long userId;
  private String name;
  private String thumbNailImg;
  Set<String> mentoringImgList;
  private int leftPeople;
  private int followers;
  private int grade;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static MentoringInfo from(Mentoring mentoring) {
    Set<String> mentoringImgUrls = mentoring.getMentoringImgList().stream()
            .map(MentoringImg::getUploadUrl)
            .collect(Collectors.toSet());

    return MentoringInfo.builder()
        .mentoringId(mentoring.getId())
        .title(mentoring.getTitle())
        .content(mentoring.getContent())
        .startDate(mentoring.getStartDate())
        .endDate(mentoring.getEndDate())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .userId(mentoring.getUser().getId())
        .name(mentoring.getUser().getName())
        .countWatch(mentoring.getCountWatch())
        .thumbNailImg(mentoring.getUploadUrl())
        .mentoringImgList(mentoringImgUrls)
        .leftPeople(6)
        .followers(12)
        .registerDate(mentoring.getRegisterDate())
        .updateDate(mentoring.getUpdateDate())
        .build();
  }

}
