package ru.practicum.main_service.events.services.private_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.BadRequestException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.requests.dto.ParticipationRequestDto;
import ru.practicum.main_service.requests.model.Request;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.main_service.events.mapper.EventMapper.*;
import static ru.practicum.main_service.locations.mapper.LocationMapper.toLocation;
import static ru.practicum.main_service.requests.mapper.RequestMapper.toEventRequestStatusUpdateResult;
import static ru.practicum.main_service.requests.mapper.RequestMapper.toParticipationRequestDtoList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventsPrivateServiceImpl implements EventsPrivateService {
    private final EventsRepository eventsRepository;
    private final UserRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public List<EventShortDto> getByUser(Long userId, Integer from, Integer size) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        List<Event> events = eventsRepository.findByInitiator(user, PageRequest.of(from / size, size)).getContent();
        Map<Long, Long> views = getViewsByEvents(events);
        Map<Long, Long> confirmationRequests = getRequestsByEvents(events);
        log.info("EventsPrivateServiceImpl: Get events by user id = {} from {} size {}", userId, from, size);
        return toEventShortDtoList(events, confirmationRequests, views);
    }

    @Transactional
    @Override
    public EventDto create(Long userId, NewEventDto newEventDto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Category category = categoriesRepository.findById((long) newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id = " + newEventDto.getCategory() + " not found."));
        Event event = toEvent(newEventDto, user, category);
        event = eventsRepository.save(event);
        log.info("EventsPrivateServiceImpl: Create new event.");
        return toEventDto(event);
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        long confirmedRequests = requestsRepository.findByEvent(event).size();
        Long views = getViewsByEvents(List.of(event)).get(eventId);
        log.info("EventsPrivateServiceImpl: Get event id = {} user id = {}", eventId, userId);
        return toEventFullDto(event, views, confirmedRequests);
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        if (!event.getState().equals(State.PENDING)) {
            throw new BadRequestException("Event id = " + eventId + " is not in pending status.");
        }
        Long views = getViewsByEvents(List.of(event)).get(eventId);
        long confirmedRequests = requestsRepository.findByEvent(event).size();
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoriesRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id = " +
                            updateEvent.getCategory() + " not found."));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (LocalDateTime.now().isBefore(updateEvent.getEventDate())) {
                event.setEventDate(updateEvent.getEventDate());
            } else {
                throw new BadRequestException("Event start date invalid.");
            }
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(toLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        log.info("EventsPrivateServiceImpl: Update event id = {} user id = {}", eventId, userId);
        return toEventFullDto(event, views, confirmedRequests);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        List<Request> requests = requestsRepository.findByEventAndStatus(event, Status.PENDING);
        log.info("EventsPrivateServiceImpl: Get requests of event id = {}", eventId);
        return toParticipationRequestDtoList(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest newRequest) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        List<Request> request = requestsRepository
                .findByEventAndIdInAndStatus(event, newRequest.getRequestIds(), Status.PENDING);
        if (request.size() != newRequest.getRequestIds().size()) {
            throw new BadRequestException("Request cannot be updated.");
        }
        for (Request r : request) {
            r.setStatus(newRequest.getStatus());
        }
        List<Request> requests = requestsRepository.findByEvent(event);
        List<Request> confirmedRequests = filterRequestsByStatus(requests, Status.CONFIRMED);
        List<Request> rejectedRequests = filterRequestsByStatus(requests, Status.REJECTED);
        if (event.getParticipantLimit() > 0 || event.isRequestModeration()) {
            if (confirmedRequests.size() > event.getParticipantLimit()) {
                throw new BadRequestException("Participation limit reached.");
            }
        } else if (event.getParticipantLimit() == confirmedRequests.size()) {
            List<Request> requestsPending = filterRequestsByStatus(requests, Status.PENDING);
            for (Request r : requestsPending) {
                r.setStatus(Status.REJECTED);
            }
        }
        log.info("EventsPrivateServiceImpl: Update request of event id = {}", eventId);
        return toEventRequestStatusUpdateResult(toParticipationRequestDtoList(confirmedRequests),
                toParticipationRequestDtoList(rejectedRequests));
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

    private List<Request> filterRequestsByStatus(List<Request> requests, Status status) {
        return requests.stream().filter(rt -> rt.getStatus().equals(status)).collect(Collectors.toList());
    }
}
