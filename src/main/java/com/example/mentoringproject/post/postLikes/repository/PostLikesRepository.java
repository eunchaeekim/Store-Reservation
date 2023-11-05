package com.example.mentoringproject.post.postLikes.repository;


import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.example.mentoringproject.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
  Optional<PostLikes> findByUserAndPost(User user, Post post);
}
