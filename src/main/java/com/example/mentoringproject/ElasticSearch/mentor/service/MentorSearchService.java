package com.example.mentoringproject.ElasticSearch.mentor.service;


import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentor.model.MentorSearchDto;
import com.example.mentoringproject.ElasticSearch.mentor.repository.MentorSearchRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorSearchService {

  private final MentorSearchRepository mentorSearchRepository;

  public List<MentorSearchDto> searchCategory(String name, String middleCategory) {
    List<MentorSearchDocumment> MentorSearchDocumments = mentorSearchRepository.findAllByNameAndMiddleCategory(
        name, middleCategory);

    return MentorSearchDocumments.stream()
        .map(MentorSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

}
