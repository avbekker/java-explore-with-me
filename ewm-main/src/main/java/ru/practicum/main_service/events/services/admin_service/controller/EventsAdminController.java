package ru.practicum.main_service.events.services.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.main_service.events.services.admin_service.service.EventsAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventsAdminController {
    private final EventsAdminService service;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAll(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<State> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) LocalDateTime rangeStart,
                                                     @RequestParam(required = false) LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("EventsAdminController: GET request received.");
        return ResponseEntity.ok(service.getAll(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable Long eventId,
                                               @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("EventsAdminController: PATCH request received for event id = {}.", eventId);
        return ResponseEntity.ok(service.update(eventId, updateEventAdminRequest));
    }
}
