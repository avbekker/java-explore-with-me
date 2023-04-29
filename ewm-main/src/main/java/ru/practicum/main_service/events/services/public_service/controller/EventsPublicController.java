package ru.practicum.main_service.events.services.public_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.enums.Sorting;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.services.public_service.service.EventsPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventsPublicController {
    private final EventsPublicService service;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> search(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) LocalDateTime rangeStart,
                                                      @RequestParam(required = false) LocalDateTime rangeEnd,
                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(required = false) Sorting sort,
                                                      @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                      @RequestParam(defaultValue = "10", required = false) @Positive Integer size,
                                                      HttpServletRequest request) {
        log.info("EventsPublicController: GET request received with searching text {} and sort by {}", text, sort);
        return ResponseEntity.ok(service.search(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Get event id:{}", id);
        return ResponseEntity.ok(service.getById(id, request));
    }
}
