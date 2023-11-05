package com.example.mentoringproject.post.img.repository;

import com.example.mentoringproject.post.img.entity.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImgRepository extends JpaRepository<PostImg, Long> {
  void deleteByPostId(Long postId);

}
