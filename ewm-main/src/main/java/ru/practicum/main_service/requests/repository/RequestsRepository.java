package ru.practicum.main_service.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.requests.model.Request;
import ru.practicum.main_service.users.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequester(User requester);

    List<Request> findAllByEventId(Long eventId);

    Optional<Request> findByRequesterAndId(User requester, Long id);

    List<Request> findByEventAndStatus(Event event, Status status);

    List<Request> findByEvent(Event events);

    List<Request> findAllByIdInAndEventId(List<Long> ids, Long eventId);

    Request findByRequesterIdAndEventId(Long userId, Long eventId);
}