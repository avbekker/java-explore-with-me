package ru.practicum.main_service.compilations.services.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.collaboration.StatsService;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.excemptions.NotFoundException;

import java.util.*;

import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilationDtoList;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventShortDtoList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final StatsService statsService;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).getContent();
        } else {
            compilations = compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).getContent();
        }
        Map<Long, List<EventShortDto>> allEvents = getEventsShortDtoMap(compilations);
        return toCompilationDtoList(compilations, allEvents);
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getById(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id = " + id + " not found."));
        Set<Event> events = compilation.getEvents();
        Map<String, Long> views = statsService.getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = statsService.getRequestsByEvents(events);
        List<EventShortDto> eventsShortDto = toEventShortDtoList(events, views, confirmationRequests);
        log.info("CompilationPublicServiceImpl: Get compilation by id = {}", id);
        return toCompilationDto(compilation, eventsShortDto);
    }

    private Map<Long, List<EventShortDto>> getEventsShortDtoMap(List<Compilation> compilations) {
        Set<Event> events = new HashSet<>();
        compilations.forEach(compilation -> events.addAll(compilation.getEvents()));
        Map<String, Long> views = statsService.getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = statsService.getRequestsByEvents(events);
        Map<Long, List<EventShortDto>> result = new HashMap<>();
        compilations.forEach(compilation -> result.put(compilation.getId(),
                toEventShortDtoList(compilation.getEvents(), views, confirmationRequests)));
        return result;
    }
}
