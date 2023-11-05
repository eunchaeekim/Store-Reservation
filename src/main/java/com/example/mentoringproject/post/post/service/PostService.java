package com.example.mentoringproject.post.post.service;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.ElasticSearch.post.repository.PostSearchRepository;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.post.img.entity.PostImg;
import com.example.mentoringproject.post.img.repository.PostImgRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.post.model.PostUpdateRequest;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostSearchRepository postSearchRepository;
  private final PostImgRepository postImgRepository;
  private final S3Service s3Service;

  // 포스팅 등록
  public Post createPost(String email, PostRegisterRequest postRegisterRequest,
      List<MultipartFile> multipartFiles) {
    User user = getUser(email);

    Post post = Post.from(user, postRegisterRequest);

    postRepository.save(post);

    postSearchRepository.save(PostSearchDocumment.fromEntity(post));

    if (multipartFiles != null) {
      List<S3FileDto> s3FileDtoList = s3Service.upload(multipartFiles, "post", "img");
      postImgRepository.saveAll(PostImg.from(s3FileDtoList, post));
    }
    return post;
  }


  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User"));
  }

  // 포스팅 수정
  @Transactional
  public Post updatePost(String email, Long postId, PostUpdateRequest postUpdateRequest,
      List<MultipartFile> multipartFiles) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));


    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    post.setCategory(postUpdateRequest.getCategory());
    post.setTitle(postUpdateRequest.getTitle());
    post.setContent(postUpdateRequest.getContent());
    post.setUpdateDatetime(LocalDateTime.now());

    postRepository.save(post);

    if (multipartFiles != null) {
      List<S3FileDto> s3FileDtoList = S3FileDto.fromEntity(post.getImgs());
      s3Service.deleteFile(s3FileDtoList);
      postImgRepository.deleteByPostId(postId);
      List<S3FileDto> s3FileDtoLists = s3Service.upload(multipartFiles, "post", "img");
      postImgRepository.saveAll(PostImg.from(s3FileDtoLists, post));
    }

    postSearchRepository.save(PostSearchDocumment.fromEntity(post));

    return post;

  }

  // 포스팅 삭제
  @Transactional
  public void deletePost(String email, Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    List<S3FileDto> s3FileDtoList = S3FileDto.fromEntity(post.getImgs());
    s3Service.deleteFile(s3FileDtoList);

    postRepository.deleteById(postId);
    postSearchRepository.deleteById(postId);
  }

  // 모든 포스트 조회
  @Transactional(readOnly = true)
  public Page<Post> findAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }
}
