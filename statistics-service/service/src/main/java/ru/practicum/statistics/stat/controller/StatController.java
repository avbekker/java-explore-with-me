package ru.practicum.statistics.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.statistics.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping(path = "/hit")
    public void create(@Validated @RequestBody HitDto hitDto) {
        log.info("StatController: Received POST request for new hit for app: {}", hitDto.getApp());
        service.create(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                             @RequestParam(required = false) List<String> uriList,
                             @RequestParam(defaultValue = "false") boolean unique) {
        log.info("StatController: Received GET request.");
        return service.get(start, end, uriList, unique);
    }
}
