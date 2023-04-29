package ru.practicum.main_service.requests.private_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.requests.dto.ParticipationRequestDto;
import ru.practicum.main_service.requests.private_service.service.RequestsPrivateService;

import java.util.List;

@Controller
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestsPrivateController {
    private final RequestsPrivateService service;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getByUser(@PathVariable Long userId) {
        log.info("RequestsPrivateController: GET request received for user with id = {}", userId);
        return ResponseEntity.ok(service.getByUser(userId));
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("RequestsPrivateController: POST request received for new request for event {} from user {}", eventId, userId);
        return new ResponseEntity<>(service.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("RequestsPrivateController: PATCH request received for request with id ={} from user {}", requestId, userId);
        return ResponseEntity.ok(service.cancel(userId, requestId));
    }
}
