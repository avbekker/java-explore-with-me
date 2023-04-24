package ru.practicum.main.requests.private_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.requests.private_service.service.RequestsPrivateService;

import java.util.Optional;

@Controller
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestsPrivateController {
    private final RequestsPrivateService service;

    @GetMapping
    public ResponseEntity<Object> getByUser(@PathVariable Long userId) {
        log.info("RequestsPrivateController: GET request received for user with id = {}", userId);
        return ResponseEntity.of(Optional.of(service.getByUser(userId)));
    }

    @PostMapping
    public ResponseEntity<Object> create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("RequestsPrivateController: POST request received for new request for event {} from user {}", eventId, userId);
        return new ResponseEntity<>(service.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("RequestsPrivateController: PATCH request received for request with id ={} from user {}", requestId, userId);
        return new ResponseEntity<>(service.cancel(userId, requestId), HttpStatus.OK);
    }
}
