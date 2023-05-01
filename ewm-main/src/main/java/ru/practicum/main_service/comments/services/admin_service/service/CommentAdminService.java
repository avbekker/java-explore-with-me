package ru.practicum.main_service.comments.services.admin_service.service;

import ru.practicum.main_service.comments.dto.CommentDto;

import java.util.List;

public interface CommentAdminService {
    List<CommentDto> getAllByUser(Long userId, Integer from, Integer size);

    void delete(Long commentId);
}
