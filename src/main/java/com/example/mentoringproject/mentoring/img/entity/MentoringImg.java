package com.example.mentoringproject.mentoring.img.entity;

import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "img_mentoring")
@EntityListeners(AuditingEntityListener.class)
public class MentoringImg {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "img_id")
  private Long id;

  private String uploadPath;
  private String uploadName;
  private String uploadUrl;

  @CreatedDate
  private LocalDateTime registerDate;

  @ManyToOne
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoring;

  public static Set<MentoringImg> from(List<S3FileDto> s3FileDto, Mentoring mentoring){
    return s3FileDto.stream()
        .map(s3File -> MentoringImg.builder()
            .mentoring(mentoring)
            .uploadName(s3File.getUploadName())
            .uploadPath(s3File.getUploadPath())
            .uploadUrl(s3File.getUploadUrl())
            .build())
        .collect(Collectors.toSet());
  }
}