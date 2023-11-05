package com.example.mentoringproject.notification.redis;

import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;

    public void publishNotification(ChannelTopic topic, NotificationDto parameter) {
        log.debug("notification save");
        NotificationDto notificationDto = notificationService.saveNotification(parameter);
        log.debug("notification convertAndSend to redis");
        redisTemplate.convertAndSend(topic.getTopic(), notificationDto);
    }
}
