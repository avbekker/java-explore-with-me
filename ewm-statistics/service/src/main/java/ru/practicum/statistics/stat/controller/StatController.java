package ru.practicum.statistics.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.statistics.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping(path = "/hit")
    public ResponseEntity<Object> create(@Validated @RequestBody HitDto hitDto) {
        log.info("StatController: Received POST request for new hit for app: {}", hitDto.getApp());
        return new ResponseEntity<>(service.create(hitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> get(@RequestParam LocalDateTime start,
                                      @RequestParam LocalDateTime end,
                                      @RequestParam(value = "uris", required = false) List<String> uriList,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        log.info("StatController: Received GET request.");
        return new ResponseEntity<>(service.get(start, end, uriList, unique), HttpStatus.OK);
    }
}
