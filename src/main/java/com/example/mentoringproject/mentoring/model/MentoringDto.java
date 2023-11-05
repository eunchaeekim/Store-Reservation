package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostDto;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MentoringDto {
  private Long mentoringId;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;
  private String uploadPath;
  private String uploadName;
  private String uploadUrl;
  private int countWatch;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static MentoringDto from(Mentoring mentoring) {
    return MentoringDto.builder()
        .mentoringId(mentoring.getId())
        .title(mentoring.getTitle())
        .content(mentoring.getContent())
        .startDate(mentoring.getStartDate())
        .endDate(mentoring.getEndDate())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .uploadPath(mentoring.getUploadPath())
        .uploadName(mentoring.getUploadName())
        .uploadUrl(mentoring.getUploadUrl())
        .countWatch(mentoring.getCountWatch())
        .registerDate(mentoring.getRegisterDate())
        .updateDate(mentoring.getUpdateDate())
        .build();
  }
}
