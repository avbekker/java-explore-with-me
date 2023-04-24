package ru.practicum.main.requests.private_service.service;

import ru.practicum.main.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestsPrivateService {
    List<ParticipationRequestDto> getByUser(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
