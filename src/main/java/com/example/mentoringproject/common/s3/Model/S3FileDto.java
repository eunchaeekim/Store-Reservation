package com.example.mentoringproject.common.s3.Model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.post.img.entity.PostImg;
import com.example.mentoringproject.post.post.entity.Post;
import java.util.Collections;
import java.util.Optional;

import com.example.mentoringproject.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class S3FileDto {
  private String uploadPath;
  private String uploadName;
  private String uploadUrl;

  public static List<S3FileDto> from(Mentoring mentoring){
    List<S3FileDto> s3FileDtoList = Optional.ofNullable(mentoring.getMentoringImgList())
        .orElse(Collections.emptyList())
        .stream()
        .map(mentoringImg -> S3FileDto.builder()
            .uploadName(mentoringImg.getUploadName())
            .uploadPath(mentoringImg.getUploadPath())
            .build())
        .collect(Collectors.toList());
    s3FileDtoList.add(S3FileDto.builder()
        .uploadName(mentoring.getUploadName())
        .uploadPath(mentoring.getUploadPath())
        .uploadUrl(mentoring.getUploadUrl()).build());
    return s3FileDtoList;
  }

  public static List<S3FileDto> from (User user){
    List<S3FileDto> s3FileDtoList = new ArrayList<>();
    s3FileDtoList.add(S3FileDto.builder()
        .uploadName(user.getUploadName())
        .uploadPath(user.getUploadPath())
        .uploadUrl(user.getUploadUrl()).build());
    return  s3FileDtoList;
  }

  public static List<S3FileDto> from(Post post){
    List<S3FileDto> s3FileDtoList = Optional.ofNullable(post.getImgs())
        .orElse(Collections.emptyList())
        .stream()
        .map(postImg -> S3FileDto.builder()
            .uploadName(postImg.getUploadName())
            .uploadPath(postImg.getUploadPath())
            .uploadUrl(postImg.getUploadUrl())
            .build())
        .collect(Collectors.toList());
    return s3FileDtoList;
  }

  public static List<S3FileDto> fromEntity(List<PostImg> postImgList) {
    return Optional.ofNullable(postImgList)
        .orElse(Collections.emptyList())
        .stream()
        .map(postImg -> S3FileDto.builder()
            .uploadName(postImg.getUploadName())
            .uploadPath(postImg.getUploadPath())
            .uploadUrl(postImg.getUploadUrl())
            .build())
        .collect(Collectors.toList());
  }

}