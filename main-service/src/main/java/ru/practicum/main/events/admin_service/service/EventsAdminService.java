package ru.practicum.main.events.admin_service.service;

import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsAdminService {
    List<EventFullDto> getAll(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
