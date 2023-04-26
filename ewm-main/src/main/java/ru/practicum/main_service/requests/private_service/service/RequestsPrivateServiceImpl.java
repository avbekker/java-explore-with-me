package ru.practicum.main_service.requests.private_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.BadRequestException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.requests.dto.ParticipationRequestDto;
import ru.practicum.main_service.requests.model.Request;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.main_service.requests.mapper.RequestMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestsPrivateServiceImpl implements RequestsPrivateService {
    private final RequestsRepository requestsRepository;
    private final UserRepository usersRepository;
    private final EventsRepository eventsRepository;

    @Override
    public List<ParticipationRequestDto> getByUser(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        List<Request> requests = requestsRepository.findByRequester(user);
        List<ParticipationRequestDto> result = toParticipationRequestDtoList(requests);
        log.info("RequestsPrivateServiceImpl: GET all participation requests for user id = {}", userId);
        return result;
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        if (user.equals(event.getInitiator())) {
            throw new BadRequestException("Initiator cannot send request.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("Event did not published yet.");
        }
        List<Request> requests = requestsRepository.findAllByEventId(eventId);
        if (event.getParticipantLimit() <= requests.size()) {
            event.setNotAvailable(true);
            throw new BadRequestException("Participation limit have been reached.");
        }
        Request result;
        if (event.isRequestModeration()) {
            result = toRequest(user, event, Status.PENDING);
        } else {
            result = toRequest(user, event, Status.CONFIRMED);
        }
        requestsRepository.save(result);
        log.info("RequestsPrivateServiceImpl: new participation request created from user id = {} to event id = {}.",
                userId, eventId);
        return toParticipationRequestDto(result);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
        Request request = requestsRepository.findByRequesterAndId(user, requestId)
                .orElseThrow(() -> new NotFoundException("Participation request with id = " + requestId +
                        " from user id = " + userId + " not found."));
        request.setStatus(Status.CANCELED);
        log.info("RequestsPrivateServiceImpl: Cancel participation request id = {} for user id = {}", requestId, userId);
        return toParticipationRequestDto(request);
    }
}
