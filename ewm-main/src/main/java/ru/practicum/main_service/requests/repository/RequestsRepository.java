package ru.practicum.main_service.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.requests.model.Request;
import ru.practicum.main_service.users.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequester(User requester);

    List<Request> findByRequesterAndEvent(User requester, Event event);

    Optional<Request> findByRequesterAndId(User requester, Long id);

    @Query("select count(r.id) from Request r where r.event = ?1 and r.status = ?2")
    Long findCount(Event event, Status status);

    List<Request> findByEventAndStatus(Event event, Status status);

    List<Request> findByEventAndStatusIn(Event event, List<Status> status);

    List<Request> findByEventInAndStatus(List<Event> events, Status status);

    List<Request> findByEvent(Event events);

    List<Request> findByEventAndIdInAndStatus(Event events, List<Long> ids, Status status);
}