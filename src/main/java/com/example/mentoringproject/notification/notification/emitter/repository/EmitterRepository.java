package com.example.mentoringproject.notification.notification.emitter.repository;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

  SseEmitter save(String emitterId, SseEmitter sseEmitter);

  Map<String, SseEmitter> findAllEmitterStartWithByUserEmail(String userEmail);

  void deleteById(String id);
}
