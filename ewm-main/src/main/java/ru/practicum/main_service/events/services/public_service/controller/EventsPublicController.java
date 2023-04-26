package ru.practicum.main_service.events.services.public_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.enums.Sorting;
import ru.practicum.main_service.events.services.public_service.service.EventsPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventsPublicController {
    private final EventsPublicService service;

    @GetMapping
    public ResponseEntity<Object> search(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean available,
                                         @RequestParam(required = false) Sorting sort,
                                         @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10", required = false) @Positive Integer size,
                                         HttpServletRequest request) {
        log.info("EventsPublicController: GET request received with searching text {} and sort by {}", text, sort);
        return new ResponseEntity<>(service.search(text, categories, paid, rangeStart, rangeEnd, available, sort,
                from, size, request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Get event id:{}", id);
        return new ResponseEntity<>(service.getById(id, request), HttpStatus.OK);
    }
}
