package ru.practicum.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.clients.HitsClient;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HitsController {
    private final HitsClient client;

    @PostMapping(path = "/hit")
    public ResponseEntity<Object> create(@Validated @RequestBody HitDto hitDto) {
        log.info("HitsController: Received POST request for new hit for app: {}", hitDto.getApp());
        return client.create(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(value = "uris", required = false) List<String> uriList,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        log.info("HitsController: Received GET request.");
        return client.get(start, end, uriList, unique);
    }
}
