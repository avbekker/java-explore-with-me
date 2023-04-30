package ru.practicum.main_service.events.services.private_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.events.services.private_service.service.EventsPrivateService;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.requests.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventsPrivateController {
    private final EventsPrivateService service;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getByUser(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("EventsPrivateController: GET request received user with id = {} from {} size {}", userId, from, size);
        return ResponseEntity.ok(service.getByUser(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventDto> create(@PathVariable Long userId, @Validated @RequestBody NewEventDto event) {
        log.info("EventsPrivateController: POST request for new event received user with id = {}", userId);
        return new ResponseEntity<>(service.create(userId, event), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventsPrivateController: GET request received user with id = {} for event id = {}", userId, eventId);
        return ResponseEntity.ok(service.getById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable Long userId, @PathVariable Long eventId,
                                               @Validated @RequestBody UpdateEventUserRequest updateEvent) {
        log.info("EventsPrivateController: PATCH request received user with id = {} for event id = {}", userId, eventId);
        return ResponseEntity.ok(service.update(userId, eventId, updateEvent));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventsPrivateController: GET request for participation received user with id = {} for event id = {}", userId, eventId);
        return ResponseEntity.ok(service.getRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                                        @RequestBody(required = false) EventRequestStatusUpdateRequest request) {
        log.info("EventsPrivateController: PATCH request for participation received user with id = {} for event id = {}", userId, eventId);
        return ResponseEntity.ok(service.updateRequestStatus(userId, eventId, request));
    }
}