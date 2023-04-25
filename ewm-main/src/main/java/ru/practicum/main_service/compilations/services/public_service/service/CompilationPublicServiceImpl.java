package ru.practicum.main_service.compilations.services.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilationDtoList;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventShortDtoList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).getContent();
        Map<Long, List<EventShortDto>> allEvents = getEventsShortDtoMap(compilations);
        return toCompilationDtoList(compilations, allEvents);
    }

    @Override
    public CompilationDto getById(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id = " + id + " not found."));
        List<Event> events = compilation.getEvents();
        Map<Long, Long> views = getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = getRequestsByEvents(events);
        List<EventShortDto> eventsShortDto = toEventShortDtoList(events, views, confirmationRequests);
        log.info("CompilationPublicServiceImpl: Get compilation by id = {}", id);
        return toCompilationDto(compilation, eventsShortDto);
    }


    private Map<Long, List<EventShortDto>> getEventsShortDtoMap(List<Compilation> compilations) {
        List<Event> events = new ArrayList<>();
        compilations.forEach(compilation -> events.addAll(compilation.getEvents()));
        Map<Long, Long> views = getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = getRequestsByEvents(events);
        Map<Long, List<EventShortDto>> result = new HashMap<>();
        compilations.forEach(compilation -> result.put(compilation.getId(), toEventShortDtoList(compilation.getEvents(), confirmationRequests, views)));
        return result;
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

    private Map<Long, Long> getViewsByEvents(List<Event> events) {
        LocalDateTime start = events.stream().map(Event::getPublishedOn).filter(Objects::nonNull)
                .min(LocalDateTime::compareTo).orElse(null);
        List<Long> eventsIds = events.stream().map(Event::getId).collect(Collectors.toList());
        return statisticsClient.getViews(eventsIds, start);
    }
}
