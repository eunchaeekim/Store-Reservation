package com.example.mentoringproject.mentoring.img.repository;

import com.example.mentoringproject.mentoring.img.entity.MentoringImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoringImgRepository extends JpaRepository<MentoringImg, Long> {
  void deleteByMentoring_Id(Long mentoringId);
}
