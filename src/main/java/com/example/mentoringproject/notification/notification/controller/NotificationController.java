package com.example.mentoringproject.notification.notification.controller;

import static com.example.mentoringproject.common.util.SpringSecurityUtil.getLoginEmail;

import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import com.example.mentoringproject.notification.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final ChannelTopic myTopic;
  private final RedisPublisher redisPublisher;

  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public ResponseEntity<SseEmitter> subscribe(@RequestParam String email) {
    log.debug("call subscribe, user={}", email);
    return ResponseEntity.ok(notificationService.subscribe(email));
  }

  @PostMapping("/publish/notification")
  public void pushMessage(@RequestBody NotificationDto notificationDto) {
    log.debug("call publish, notification={}", notificationDto);
    redisPublisher.publishNotification(myTopic, notificationDto);
  }


  // 나에게온 알림 조회하기 10개씩 조회 (최신순으로 정렬)
  @GetMapping("/notification")
  public ResponseEntity<Page<NotificationDto>> getNotification(
      @PageableDefault(size = 10, sort = "registerDate", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    String email = getLoginEmail();
    return ResponseEntity.ok(notificationService.getNotification(email, pageable));
  }

  //알림 읽기
  @PutMapping("/read/notification")
  public ResponseEntity<?> readNotification(@RequestParam("notificationId") Long notificationId) {
    return ResponseEntity.ok(notificationService.readNotification(notificationId));
  }

  //알림 삭제하기
  @DeleteMapping("/notification")
  public ResponseEntity<Boolean> deleteNotification(
      @RequestParam("notificationId") Long notificationId) {
    notificationService.deleteNotification(notificationId);
    return ResponseEntity.ok(true);
  }
}
