package ru.practicum.main_service.comments.services.public_service.service;

import ru.practicum.main_service.comments.dto.CommentDto;

import java.util.List;

public interface CommentPublicService {
    List<CommentDto> getByEvent(Long eventId, Integer from, Integer size);
}
