package ru.practicum.main_service.comments.services.public_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.services.public_service.service.CommentPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentPublicController {
    private final CommentPublicService service;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getByEvent(@PathVariable Long eventId,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("CommentPublicController: GET request received for all comments of event id = {}", eventId);
        return ResponseEntity.ok(service.getByEvent(eventId, from, size));
    }
}
