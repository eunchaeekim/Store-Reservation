package com.example.mentoringproject.notification.notification.emitter.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {

  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
    emitters.put(emitterId, sseEmitter);
    return sseEmitter;
  }

  @Override
  public Map<String, SseEmitter> findAllEmitterStartWithByUserEmail(String userEmail) {
    return emitters.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userEmail))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public void deleteById(String id) {
    emitters.remove(id);
  }

}
