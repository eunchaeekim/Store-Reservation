package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.model.MentoringList;
import com.example.mentoringproject.mentoring.service.MentoringService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
  private final MentoringService mentoringService;

  @PostMapping
  public ResponseEntity<MentoringDto> createMentoring(
      @RequestPart MentoringDto mentoringDto,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFiles
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.createMentoring(email, mentoringDto, thumbNailImg, multipartFiles)));
  }

  @PutMapping("/{mentoringId}")
  public ResponseEntity<MentoringDto> updateMentoring(
      @PathVariable Long mentoringId,
      @RequestPart MentoringDto mentoringDto,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFiles
  ) {

    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.updateMentoring(email, mentoringId, mentoringDto, thumbNailImg, multipartFiles)));
  }

  @DeleteMapping("/{mentoringId}")
  public ResponseEntity<Void> deleteMentoring(
      @PathVariable Long mentoringId
  ) {
    mentoringService.deleteMentoring(mentoringId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{mentoringId}")
  public ResponseEntity<MentoringInfo> MentoringInfo(
      @PathVariable Long mentoringId
  ) {
    return ResponseEntity.ok(MentoringInfo.from(mentoringService.mentoringInfo(mentoringId)));
  }

  @GetMapping
  public ResponseEntity<Page<MentoringList>> getMentoringList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "5") int pageSize,
          @RequestParam(defaultValue = "id") String sortId,
          @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);

    return ResponseEntity.ok(MentoringList.from(mentoringService.getMentoringList(pageable)));
}
  @GetMapping("/main")
  public ResponseEntity<Map<String, List<?>>> getMentoringMain() {
    Map<String, List<?>> mentoringMainPageDtoMap = new HashMap<>();
    mentoringMainPageDtoMap.put("MentoringByCountWatch", mentoringService.getMentoringByCountWatch());
    mentoringMainPageDtoMap.put("MentorByRating", mentoringService.getMentorByRating());
    mentoringMainPageDtoMap.put("PostRegisterDateTime", mentoringService.getPostByRegisterDateTime());
    mentoringMainPageDtoMap.put("MentoringByEndDate", mentoringService.getMentoringByEndDate());
    return ResponseEntity.ok(mentoringMainPageDtoMap);
  }
}
