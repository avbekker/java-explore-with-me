package ru.practicum.main_service.comments.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.events.model.Event;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select com.event.id, count(com) from Comment com " +
            "where com.event.id = ?1 group by com.event.id")
    Map<Long, Integer> findAllCommentsByEvents(List<Long> eventsId);

    List<Comment> findAllByCreatorId(Long creatorId, PageRequest pageRequest);

    List<Comment> findAllByEvent(Event event, PageRequest pageRequest);
}
