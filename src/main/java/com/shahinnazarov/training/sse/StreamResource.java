package com.shahinnazarov.training.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*")
public class StreamResource {

    @Autowired
    private EventBusService service;

    @GetMapping("/stream")
    public ResponseEntity<SseEmitter> doNotify() throws IOException {
        final SseEmitter emitter = new SseEmitter();
        service.addEmitter(emitter);
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }


    @GetMapping("/session")
    public ResponseEntity<String> sessions() {
        return ResponseEntity.ok(String.format("Connected Users: %d",  service.emitters.size()));
    }




}
