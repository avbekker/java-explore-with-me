package ru.practicum.main_service.comments.services.private_service.service;

import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentPrivateService {
    CommentDto create(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto update(Long userId, Long commentId, NewCommentDto commentDto);

    CommentDto getById(Long userId, Long commentId);

    List<CommentDto> getAll(Long userId, Integer from, Integer size);
}
