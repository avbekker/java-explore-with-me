package ru.practicum.main.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.enums.State;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.users.model.User;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByCategoryId(Long categoryId);

    Page<Event> findByInitiator(User initiator, Pageable pageable);

    List<Event> findByIdInAndState(List<Long> eventsId, State state);

    List<Event> findByIdIn(List<Long> eventsId);

    Optional<Event> findByInitiatorAndId(User initiator, Long eventId);
}
