package ru.practicum.main_service.requests.mapper;

import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.requests.dto.ParticipationRequestDto;
import ru.practicum.main_service.requests.model.Request;
import ru.practicum.main_service.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static Request toRequest(User user, Event event, Status status) {
        return Request.builder()
                .id(null)
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests,
                                                                                  List<ParticipationRequestDto> rejectedRequests) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    public static List<ParticipationRequestDto> toParticipationRequestDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private RequestMapper() {
    }
}
