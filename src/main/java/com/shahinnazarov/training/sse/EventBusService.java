package com.shahinnazarov.training.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EventBusService {

    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(final SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }

    @Async
    @Scheduled(fixedRate = 5000)
    public void doNotify() throws IOException {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        String s = UUID.randomUUID().toString();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .data(s));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
}
