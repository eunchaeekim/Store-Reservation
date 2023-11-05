package com.example.mentoringproject.ElasticSearch.mentoring.service;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentoring.model.MentoringSearchDto;
import com.example.mentoringproject.ElasticSearch.mentoring.repository.MentoringSearchRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringSearchService {

  private final MentoringSearchRepository metoringSearchRepository;

  public List<MentoringSearchDto> searchTitleAndCategory(String title, String category) {
    List<MentoringSearchDocumment> MentoringSearchDocumments = metoringSearchRepository.findAllByTitleContainingAndCategory(
        title, category);

    return MentoringSearchDocumments.stream()
        .map(MentoringSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

  public List<MentoringSearchDto> searchWriterAndCategory(String writer, String category) {
    List<MentoringSearchDocumment> MentoringSearchDocumments = metoringSearchRepository.findAllByWriterAndCategory(
        writer, category);

    return MentoringSearchDocumments.stream()
        .map(MentoringSearchDto::fromDocument)
        .collect(Collectors.toList());
  }
}
