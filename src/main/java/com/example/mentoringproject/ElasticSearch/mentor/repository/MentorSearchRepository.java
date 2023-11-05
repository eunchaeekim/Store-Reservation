package com.example.mentoringproject.ElasticSearch.mentor.repository;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MentorSearchRepository extends
    ElasticsearchRepository<MentorSearchDocumment, Long> {

  List<MentorSearchDocumment> findAllByNameAndMiddleCategory(String name, String middleCategory);

  void deleteByName(String name);
}