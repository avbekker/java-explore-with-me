package ru.practicum.main_service.events.services.admin_service.service;

import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsAdminService {
    List<EventFullDto> getAll(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
