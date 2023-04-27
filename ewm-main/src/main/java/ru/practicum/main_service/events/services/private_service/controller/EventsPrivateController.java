package ru.practicum.main_service.events.services.private_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.dto.UpdateEventUserRequest;
import ru.practicum.main_service.events.services.private_service.service.EventsPrivateService;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventsPrivateController {
    private final EventsPrivateService service;

    @GetMapping
    public ResponseEntity<Object> getByUser(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("EventsPrivateController: GET request received user with id = {} from {} size {}", userId, from, size);
        return new ResponseEntity<>(service.getByUser(userId, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> create(@PathVariable Long userId, @Validated @RequestBody NewEventDto event) {
        log.info("EventsPrivateController: POST request for new event received user with id = {}", userId);
        return new ResponseEntity<>(service.create(userId, event), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventsPrivateController: GET request received user with id = {} for event id = {}", userId, eventId);
        return new ResponseEntity<>(service.getById(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @PathVariable Long eventId,
                                         @Validated @RequestBody UpdateEventUserRequest updateEvent) {
        log.info("EventsPrivateController: PATCH request received user with id = {} for event id = {}", userId, eventId);
        return new ResponseEntity<>(service.update(userId, eventId, updateEvent), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Object> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventsPrivateController: GET request for participation received user with id = {} for event id = {}", userId, eventId);
        return new ResponseEntity<>(service.getRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<Object> updateRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("EventsPrivateController: PATCH request for participation received user with id = {} for event id = {}", userId, eventId);
        return new ResponseEntity<>(service.updateRequestStatus(userId, eventId, request), HttpStatus.OK);
    }
}
