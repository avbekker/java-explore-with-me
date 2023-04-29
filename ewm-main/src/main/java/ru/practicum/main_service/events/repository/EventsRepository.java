package ru.practicum.main_service.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventsRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e " +
            "where (:users IS NULL or e.initiator.id IN :users) " +
            "and (:states IS NULL or e.state IN :states) " +
            "and (:categories IS NULL or e.category.id IN :categories) " +
            "and (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    Page<Event> getEventsByAdmin(@Param("users") List<Long> users,
                                 @Param("states") List<State> states,
                                 @Param("categories") List<Long> categories,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where (upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "or upper(e.description) like upper(concat('%', :text, '%'))) " +
            "and (:paid IS NULL OR e.paid = :paid) " +
            "and e.eventDate between :rangeStart and :rangeEnd " +
            "and e.state = :state " +
            "and (:categories IS NULL OR e.category.id IN :categories) ")
    Page<Event> getEventPublicSearch(@Param("text") String text,
                                     @Param("categories") Collection<Long> categories,
                                     @Param("paid") Boolean paid,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     @Param("state") State state,
                                     Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);

    Page<Event> findByInitiator(User initiator, Pageable pageable);

    Set<Event> findByIdInAndState(List<Long> eventsId, State state);

    Set<Event> findByIdIn(List<Long> eventsId);

    Optional<Event> findByInitiatorAndId(User initiator, Long eventId);
}
