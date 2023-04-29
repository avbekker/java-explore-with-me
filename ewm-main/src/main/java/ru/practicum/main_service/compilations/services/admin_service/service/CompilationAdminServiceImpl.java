package ru.practicum.main_service.compilations.services.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.collaboration.StatsService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilation;
import static ru.practicum.main_service.compilations.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventShortDtoList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;
    private final StatsService statsService;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            compilation = toCompilation(newCompilationDto, Set.of());
            compilationRepository.save(compilation);
            log.info("CompilationAdminServiceImpl: New compilation created.");
            return toCompilationDto(compilation, List.of());
        }
        Set<Event> events = eventsRepository.findByIdIn(newCompilationDto.getEvents());
        compilation = toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> eventsShortDto = getEventShortDtoList(events);
        log.info("CompilationAdminServiceImpl: New compilation created.");
        return toCompilationDto(compilation, eventsShortDto);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id = " + compilationId + " not found."));
        Set<Event> events = compilation.getEvents();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventsRepository.findByIdInAndState(updateCompilationRequest.getEvents(), State.PUBLISHED);
        }
        List<EventShortDto> eventsShortDto = getEventShortDtoList(events);
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

    private List<EventShortDto> getEventShortDtoList(Set<Event> events) {
        Map<String, Long> views = statsService.getViewsByEvents(new ArrayList<>(events));
        Map<Long, Long> confirmationRequests = statsService.getRequestsByEvents(events);
        return toEventShortDtoList(events, views, confirmationRequests);
    }
}
