package com.example.mentoringproject.ElasticSearch.mentoring.repository;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MentoringSearchRepository extends
    ElasticsearchRepository<MentoringSearchDocumment, Long> {

  List<MentoringSearchDocumment> findAllByTitleContainingAndCategory(String title, String category);

  List<MentoringSearchDocumment> findAllByWriterAndCategory(String writer, String category);
}