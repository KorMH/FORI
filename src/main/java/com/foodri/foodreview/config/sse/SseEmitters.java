package com.foodri.foodreview.config.sse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitters {

  private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter addEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(60 * 1000L); // 60초 동안 유지
    emitters.put(userId, emitter);

    // 클라이언트가 연결을 종료했을 때 처리
    emitter.onCompletion(() -> emitters.remove(userId));
    emitter.onTimeout(() -> emitters.remove(userId));

    return emitter;
  }

  public void sendNotification(Long userId, String message) {
    SseEmitter emitter = emitters.get(userId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event()
            .name("notification")
            .data(message));
      } catch (IOException e) {
        emitters.remove(userId);
      }
    }
  }
}
