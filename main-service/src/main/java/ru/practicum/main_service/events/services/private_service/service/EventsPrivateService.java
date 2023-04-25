package ru.practicum.main_service.events.services.private_service.service;

import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface EventsPrivateService {
    List<EventShortDto> getByUser(Long userId, Integer from, Integer size);

    EventDto create(Long userId, NewEventDto event);

    EventFullDto getById(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
