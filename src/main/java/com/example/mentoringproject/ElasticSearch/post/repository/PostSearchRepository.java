package com.example.mentoringproject.ElasticSearch.post.repository;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PostSearchRepository extends ElasticsearchRepository<PostSearchDocumment, Long> {

  List<PostSearchDocumment> findAllByWriter(String writer);

  List<PostSearchDocumment> findAllByTitleContaining(String title);

  List<PostSearchDocumment> findAllById(Long Id);

}