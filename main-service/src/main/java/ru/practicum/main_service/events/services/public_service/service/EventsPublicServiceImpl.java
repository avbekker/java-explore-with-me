package ru.practicum.main_service.events.services.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.BadRequestException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.main_service.events.mapper.EventMapper.toEventFullDto;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventShortDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventsPublicServiceImpl implements EventsPublicService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean available, String sort, Integer from, Integer size,
                                      HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Start cannot be after end.");
            }
        }
        Sort.TypedSort<Event> eventSort = Sort.sort(Event.class);
        Sort sorting;
        Pageable pageable;
        switch (sort) {
            case "EVENT_DATE":
                sorting = eventSort.by(Event::getEventDate).descending();
                pageable = PageRequest.of(from / size, size, sorting);
                break;
            case "VIEWS":
                pageable = Pageable.unpaged();
                break;
            default:
                throw new BadRequestException("Unavailable sorting option.");
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(1L);
        }
        List<Event> events = eventsRepository.getEventPublicSearch(text, categories, paid, rangeStart, rangeEnd,
                available, State.PUBLISHED, pageable).getContent();
        Map<Long, Long> views = getViewsByEvents(events);
        Map<Long, Long> confirmedRequests = getRequestsByEvents(events);
        log.info("EventsPublicServiceImpl: Get events by text {}", text);
        return toEventShortDtoList(events, confirmedRequests, views);
    }

    @Override
    public EventFullDto getById(Long id, HttpServletRequest request) {
        Event event = eventsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id = " + id + " not found."));
        HitDto hitDto = new HitDto("main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        statisticsClient.create(hitDto);
        long confirmedRequests = requestsRepository.findByEvent(event).size();
        Long views = getViewsByEvents(List.of(event)).get(id);
        log.info("EventsPublicServiceImpl: Get event by id = {}", id);
        return toEventFullDto(event, views, confirmedRequests);
    }

    private Map<Long, Long> getViewsByEvents(List<Event> events) {
        LocalDateTime start = events.stream().map(Event::getPublishedOn).filter(Objects::nonNull)
                .min(LocalDateTime::compareTo).orElse(null);
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        return statisticsClient.getViews(eventIds, start);
    }

    private Map<Long, Long> getRequestsByEvents(List<Event> events) {
        Map<Long, Long> confirmedRequestsByEvents = new HashMap<>();
        long confirmationRequests;
        for (Event event : events) {
            confirmationRequests = requestsRepository.findByEvent(event).size();
            confirmedRequestsByEvents.put(event.getId(), confirmationRequests);
        }
        return confirmedRequestsByEvents;
    }
}
