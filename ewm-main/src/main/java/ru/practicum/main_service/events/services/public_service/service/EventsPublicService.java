package ru.practicum.main_service.events.services.public_service.service;

import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventsPublicService {
    List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                               Boolean available, String sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getById(Long id, HttpServletRequest request);
}
