package ru.practicum.main_service.comments.services.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.services.admin_service.service.CommentAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping("/admin/comments/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentAdminController {
    private final CommentAdminService service;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CommentDto>> getAllByUser(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("CommentAdminController: GET request received for all comment by user id = {}", userId);
        return ResponseEntity.ok(service.getAllByUser(userId, from, size));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        log.info("CommentAdminController: DELETE request received for comment id = {}", commentId);
        service.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
