package ru.practicum.main_service.events.mapper;

import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.dto.EventDto;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.main_service.categories.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.main_service.users.mapper.UserMapper.toUserShortDto;

public class EventMapper {
    private EventMapper() {
    }

    public static Event toEvent(NewEventDto newEventDto, User user, Category category) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.isRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .compilations(null)
                .build();
    }

    public static EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .location(event.getLocation())
                .requestModeration(event.isRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, Long views, Integer confirmedRequests) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .requestModeration(event.isRequestModeration())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Integer confirmedRequests, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .views(views)
                .build();
    }

    public static List<EventFullDto> toEventFullDtoList(Set<Event> events, Map<String, Long> viewsByEvents,
                                                        Map<Long, Integer> confirmedRequestsByEvents) {
        return events.stream()
                .map(event -> toEventFullDto(event,
                        viewsByEvents.get(String.format("/events/%s", event.getId())),
                        confirmedRequestsByEvents.get(event.getId()))).collect(Collectors.toList());
    }

    public static List<EventShortDto> toEventShortDtoList(Set<Event> events, Map<String, Long> viewsByEvents,
                                                          Map<Long, Integer> confirmedRequestsByEvents) {
        return events.stream()
                .map(event -> toEventShortDto(event, confirmedRequestsByEvents.get(event.getId()),
                        viewsByEvents.get(String.format("/events/%s", event.getId())))).collect(Collectors.toList());
    }
}
