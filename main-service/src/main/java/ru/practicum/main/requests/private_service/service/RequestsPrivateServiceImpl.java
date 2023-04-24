package ru.practicum.main.requests.private_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.enums.State;
import ru.practicum.main.enums.Status;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.excemptions.BadRequestException;
import ru.practicum.main.excemptions.NotFoundException;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.model.Request;
import ru.practicum.main.requests.repository.RequestsRepository;
import ru.practicum.main.users.model.User;
import ru.practicum.main.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.main.requests.mapper.RequestMapper.*;

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
        Event event = eventsRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        if (user.equals(event.getInitiator())) {
            throw new BadRequestException("Initiator cannot send request.");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("Event did not published yet.");
        }
        List<Request> requests = requestsRepository.findByRequesterAndEvent(user, event);
        if (requests.isEmpty()) {
            Request result;
            List<Request> requestsOfEvent = requestsRepository.findByEventAndStatusIn(event, List.of(Status.CONFIRMED));
            if (event.getParticipantLimit() <= requestsOfEvent.size()) {
                event.setNotAvailable(true);
                throw new BadRequestException("Participation limit have been reached.");
            }
            if (event.isRequestModeration()) {
                result = toRequest(user, event, Status.PENDING);
            } else {
                result = toRequest(user, event, Status.CONFIRMED);
            }
            log.info("RequestsPrivateServiceImpl: new participation request created from user id = {} to event id = {}.",
                    userId, eventId);
            return toParticipationRequestDto(requestsRepository.save(result));
        } else {
            throw new BadRequestException("Participation request already created.");
        }
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
