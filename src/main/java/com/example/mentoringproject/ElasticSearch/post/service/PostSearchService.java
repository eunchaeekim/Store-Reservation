package com.example.mentoringproject.ElasticSearch.post.service;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.ElasticSearch.post.model.PostSearchDto;
import com.example.mentoringproject.ElasticSearch.post.repository.PostSearchRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostSearchService {

  private final PostSearchRepository postSearchRepository;

  public List<PostSearchDto> searchWriter(String writer) {
    List<PostSearchDocumment> postSearchDocummentList = postSearchRepository.findAllByWriter(
        writer);

    return postSearchDocummentList.stream()
        .map(PostSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

  public List<PostSearchDto> searchTitle(String title) {
    List<PostSearchDocumment> elasticSearchPostList = postSearchRepository.findAllByTitleContaining(
        title);

    return elasticSearchPostList.stream()
        .map(PostSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

}