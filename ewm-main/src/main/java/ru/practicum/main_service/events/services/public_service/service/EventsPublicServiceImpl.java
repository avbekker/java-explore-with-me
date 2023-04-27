package ru.practicum.main_service.events.services.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.main_service.collaboration.StatsService;
import ru.practicum.main_service.enums.Sorting;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.BadRequestException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.repository.RequestsRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static ru.practicum.main_service.events.mapper.EventMapper.toEventFullDto;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventShortDtoList;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsPublicServiceImpl implements EventsPublicService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatsService statsService;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean available, Sorting sort, Integer from, Integer size,
                                      HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Start cannot be after end.");
            }
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventsRepository.getEventPublicSearch(text, categories, paid, rangeStart, rangeEnd,
                available, State.PUBLISHED, pageable).getContent();
        Map<String, Long> views = statsService.getViewsByEvents(events);
        Map<Long, Long> confirmedRequests = statsService.getRequestsByEvents(events);
        List<EventShortDto> result = toEventShortDtoList(events, views, confirmedRequests);
        if (sort != null) {
            if (sort.equals(Sorting.EVENT_DATE)) {
                result.sort(Comparator.comparing(EventShortDto::getEventDate));
            } else if (sort.equals(Sorting.VIEWS)) {
                result.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        statsService.createHit(HitDto.builder()
                .app("main-container")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .created(LocalDateTime.now())
                .build());
        log.info("EventsPublicServiceImpl: Get events by text {}", text);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getById(Long id, HttpServletRequest request) {
        Event event = eventsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id = " + id + " not found."));
        HitDto hitDto = new HitDto("main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        statsService.createHit(hitDto);
        long confirmedRequests = requestsRepository.findByEvent(event).size();
        Long views = statsService.getViewsByEvents(List.of(event)).get(String.format("/events/%s", id));
        log.info("EventsPublicServiceImpl: Get event by id = {}", id);
        return toEventFullDto(event, views, confirmedRequests);
    }
}
