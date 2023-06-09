package ru.practicum.main_service.events.services.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.collaboration.StatsService;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.enums.StateActionAdmin;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.BadRequestException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.repository.RequestsRepository;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.main_service.events.mapper.EventMapper.toEventFullDto;
import static ru.practicum.main_service.events.mapper.EventMapper.toEventFullDtoList;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsAdminServiceImpl implements EventsAdminService {
    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final StatsService statsService;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getAll(List<Long> users, List<State> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Set<Event> events = new HashSet<>(eventsRepository.getEventsByAdmin(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from / size, size)).getContent());
        Map<String, Long> viewsByEvents = statsService.getViewsByEvents(new ArrayList<>(events));
        Map<Long, Integer> confirmedRequestsByEvents = statsService.getRequestsByEvents(events);
        return toEventFullDtoList(events, viewsByEvents, confirmedRequestsByEvents);
    }

    @Transactional
    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        if (!event.getState().equals(State.PENDING)) {
            throw new BadRequestException("Event id = " + eventId + " is not in pending status.");
        }
        if (event.getCreatedOn() != null && updateEvent.getEventDate() != null
                && updateEvent.getEventDate().plusHours(1).isBefore(event.getCreatedOn())) {
            throw new BadRequestException("Cannot update event. Creation date " + event.getCreatedOn() +
                    " should be earlier than event date " + updateEvent.getEventDate());
        }
        event = updateEvent(event, updateEvent);
        Long views = statsService.getViewsByEvents(List.of(event)).get(String.format("/events/%s", eventId));
        int confirmedRequests = requestsRepository.findByEvent(event).size();
        return toEventFullDto(event, views, confirmedRequests);
    }

    private Event updateEvent(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now().withNano(0));
            event.setRequestModeration(true);
        } else if (updateEvent.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        event.setEventDate(Objects.requireNonNullElse(updateEvent.getEventDate(), event.getEventDate()));
        event.setPaid(Objects.requireNonNullElse(updateEvent.getPaid(), event.getPaid()));
        event.setParticipantLimit(Objects.requireNonNullElse(updateEvent.getParticipantLimit(), event.getParticipantLimit()));
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        event.setParticipantLimit(Objects.requireNonNullElse(updateEvent.getParticipantLimit(), event.getParticipantLimit()));
        event.setRequestModeration(Objects.requireNonNullElse(updateEvent.getRequestModeration(), event.isRequestModeration()));
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoriesRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id = " +
                            updateEvent.getCategory() + " not found."));
            event.setCategory(category);
        }
        event = eventsRepository.save(event);
        return event;
    }
}
