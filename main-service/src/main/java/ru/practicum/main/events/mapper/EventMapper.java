package ru.practicum.main.events.mapper;

import ru.practicum.main.categories.model.Category;
import ru.practicum.main.enums.State;
import ru.practicum.main.events.dto.EventDto;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.locations.model.Location;
import ru.practicum.main.users.model.User;

import java.time.LocalDateTime;

import static ru.practicum.main.categories.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.main.locations.mapper.LocationMapper.toLocationDto;
import static ru.practicum.main.users.mapper.UserMapper.toUserShortDto;

public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, User user, Category category, Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(location)
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.isRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .compilations(null)
                .isNotAvailable(false)
                .build();
    }

    public static EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .paid(event.isPaid())
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .location(toLocationDto(event.getLocation()))
                .requestModeration(event.isRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, Long views, Long confirmedRequests) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .requestModeration(event.isRequestModeration())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .paid(event.isPaid())
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .views(views)
                .build();
    }

    private EventMapper() {
    }
}
