package ru.practicum.main_service.comments.services.private_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.NewCommentDto;
import ru.practicum.main_service.comments.services.private_service.service.CommentPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Validated
public class CommentPrivateController {
    private final CommentPrivateService service;

    @PostMapping
    public ResponseEntity<CommentDto> create(@PathVariable Long userId, @RequestParam Long eventId,
                                             @Valid @RequestBody NewCommentDto commentDto) {
        log.info("CommentPrivateController: POST request received for new comment {}, " +
                "from user id = {} for event id = {}", commentDto, userId, eventId);
        return new ResponseEntity<>(service.create(userId, eventId, commentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long userId, @PathVariable Long commentId,
                                             @RequestBody NewCommentDto commentDto) {
        log.info("CommentPrivateController: PATCH request received for update comment id = {}, " +
                "from user id = {}", commentId, userId);
        return ResponseEntity.ok(service.update(userId, commentId, commentDto));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("CommentPrivateController: GET request received for comment id = {}, " +
                "from user id = {}", commentId, userId);
        return ResponseEntity.ok(service.getById(userId, commentId));
    }

    @GetMapping()
    public ResponseEntity<List<CommentDto>> getAll(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("CommentAdminController: GET request received for all comment by user id = {}", userId);
        return ResponseEntity.ok(service.getAll(userId, from, size));
    }
}
