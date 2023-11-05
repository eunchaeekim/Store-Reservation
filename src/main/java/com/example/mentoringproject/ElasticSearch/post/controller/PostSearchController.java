package com.example.mentoringproject.ElasticSearch.post.controller;

import com.example.mentoringproject.ElasticSearch.post.model.PostSearchDto;
import com.example.mentoringproject.ElasticSearch.post.service.PostSearchService;
import com.example.mentoringproject.post.post.service.PostService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostSearchController {

  private final PostService postService;
  private final PostSearchService postSearchService;

  @PostMapping("/search")
  public ResponseEntity<List<PostSearchDto>> searchPost(
      @RequestParam(required = true) String searchType,
      @RequestParam(required = true) String searchText,
      @RequestParam(required = true) String sortBy,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "4") int pageSize) {

    List<PostSearchDto> postSearchDtoList = new ArrayList<>();

    if ("title".equals(searchType)) {
      postSearchDtoList = postSearchService.searchTitle(searchText);
    } else if ("content".equals(searchType)) {
      postSearchDtoList = postSearchService.searchWriter(searchText);
    } else {
      return ResponseEntity.badRequest().build();
    }

    // 인기순, 최신순 정렬
    if ("likes".equals(sortBy)) {
      postSearchDtoList.sort(
          Comparator.comparing(PostSearchDto::getPostLikesCounty).reversed());
    } else if ("latest".equals(sortBy)) {
      postSearchDtoList.sort(Comparator.comparing(PostSearchDto::getId).reversed());
    }

    // 페이징 처리
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, postSearchDtoList.size());

    List<PostSearchDto> pagedResult = postSearchDtoList.subList(start, end);

    return ResponseEntity.ok(pagedResult);

  }
}
