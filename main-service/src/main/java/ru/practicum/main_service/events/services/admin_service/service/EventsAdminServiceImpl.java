package ru.practicum.main_service.events.services.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.BadRequestException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.main_service.events.mapper.EventMapper.toEventFullDto;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventFullDtoList;
import static ru.practicum.main_service.locations.mapper.LocationMapper.toLocation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventsAdminServiceImpl implements EventsAdminService {
    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public List<EventFullDto> getAll(List<Long> users, List<String> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        List<Event> events = eventsRepository.getEventsByAdmin(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from / size, size)).getContent();
        Map<Long, Long> viewsByEvents = getViewsByEvents(events);
        Map<Long, Long> confirmedRequestsByEvents = getRequestsByEvents(events);
        return toEventFullDtoList(events, confirmedRequestsByEvents, viewsByEvents);
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        if (!event.getState().equals(State.PENDING)) {
            throw new BadRequestException("Event id = " + eventId + " is not in pending status.");
        }
        Long views = getViewsByEvents(List.of(event)).get(eventId);
        long confirmedRequests = requestsRepository.findByEvent(event).size();
        if (updateEventAdminRequest.getAnnotation() != null && !updateEventAdminRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoriesRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id = " +
                            updateEventAdminRequest.getCategory() + " not found."));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null && !updateEventAdminRequest.getDescription().isBlank()) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (LocalDateTime.now().isBefore(updateEventAdminRequest.getEventDate())) {
                event.setEventDate(updateEventAdminRequest.getEventDate());
            } else {
                throw new BadRequestException("Event start date invalid.");
            }
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(toLocation(updateEventAdminRequest.getLocation()));
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null && !updateEventAdminRequest.getTitle().isBlank()) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return toEventFullDto(event, views, confirmedRequests);
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
