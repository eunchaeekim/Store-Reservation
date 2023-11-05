package com.example.mentoringproject.mentoring.entity;

import com.example.mentoringproject.mentoring.img.entity.MentoringImg;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "mentoring")
@EntityListeners(AuditingEntityListener.class)
public class Mentoring {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mentoring_id")
  private Long id;

  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;

  @Enumerated(EnumType.STRING)
  private MentoringStatus status;
  private String category;

  private String uploadPath;
  private String uploadName;
  private String uploadUrl;


  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private int countWatch;

  @OneToMany(mappedBy = "mentoring")
  private List<MentoringImg> mentoringImgList = new ArrayList<>();


  @CreatedDate
  private LocalDateTime registerDate;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;


  public static Mentoring from(User user, MentoringDto mentoringDto) {
    return Mentoring.builder()
        .title(mentoringDto.getTitle())
        .content(mentoringDto.getContent())
        .startDate(mentoringDto.getStartDate())
        .endDate(mentoringDto.getEndDate())
        .numberOfPeople(mentoringDto.getNumberOfPeople())
        .amount(mentoringDto.getAmount())
        .status(mentoringDto.getStatus())
        .category(mentoringDto.getCategory())
        .uploadPath(mentoringDto.getUploadPath())
        .uploadName(mentoringDto.getUploadName())
        .uploadUrl(mentoringDto.getUploadUrl())
        .user(user)
        .build();
  }
}
