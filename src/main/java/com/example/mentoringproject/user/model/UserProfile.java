package com.example.mentoringproject.user.model;


import com.example.mentoringproject.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Getter
@Builder
public class UserProfile {

  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String imgUrl;

  public static UserProfile from(User user){
    return UserProfile.builder()
        .name(user.getName())
        .career(user.getCareer())
        .introduce(user.getIntroduce())
        .mainCategory(user.getMainCategory())
        .middleCategory(user.getMiddleCategory())
        .imgUrl(user.getUploadUrl())
        .build();
  }

  public static Page<UserProfile> from(Page<User> page){
   List<UserProfile> userProfiles =  page.getContent().stream()
        .map(user -> UserProfile.builder()
            .name(user.getName())
            .career(user.getCareer())
            .introduce(user.getIntroduce())
            .mainCategory(user.getMainCategory())
            .middleCategory(user.getMiddleCategory())
            .imgUrl(user.getUploadUrl())
            .build()
        ).collect(Collectors.toList());

    return new PageImpl<>(userProfiles, page.getPageable(), page.getTotalElements());

  }
}
