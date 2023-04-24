package ru.practicum.main.events.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.clients.StatisticsClient;
import ru.practicum.dto.StatDto;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequest;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.requests.model.Request;
import ru.practicum.main.requests.repository.RequestsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventsAdminServiceImpl implements EventsAdminService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public List<EventFullDto> getAll(List<Long> users, List<String> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        List<Event> events;
        List<StatDto> views;
        List<Request> requests;


        return null;
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        return null;
    }
}
