package ru.practicum.main_service.compilations.services.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilation;
import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventShortDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            compilation = toCompilation(newCompilationDto, List.of());
            compilationRepository.save(compilation);
            log.info("CompilationAdminServiceImpl: New compilation created.");
            return toCompilationDto(compilation, List.of());
        }
        List<Event> events = eventsRepository.findByIdIn(newCompilationDto.getEvents());
        compilation = toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        Map<Long, Long> views = getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = getRequestsByEvents(events);
        List<EventShortDto> eventsShortDto = toEventShortDtoList(events, views, confirmationRequests);
        log.info("CompilationAdminServiceImpl: New compilation created.");
        return toCompilationDto(compilation, eventsShortDto);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id = " + compilationId + " not found."));
        List<Event> events = compilation.getEvents();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventsRepository.findByIdInAndState(updateCompilationRequest.getEvents(), State.PUBLISHED);
        }
        Map<Long, Long> views = getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = getRequestsByEvents(events);
        List<EventShortDto> eventsShortDto = toEventShortDtoList(events, views, confirmationRequests);
        compilation.setEvents(events);
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        log.info("CompilationAdminServiceImpl: compilation with id = {} updated..", compilationId);
        return toCompilationDto(compilation, eventsShortDto);
    }

    @Transactional
    @Override
    public void delete(Long compilationId) {
        compilationRepository.deleteById(compilationId);
        log.info("CompilationAdminServiceImpl: compilation id = {} deleted.", compilationId);
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
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        return statisticsClient.getViews(eventIds, start);
    }
}
