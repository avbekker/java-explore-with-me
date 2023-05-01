package ru.practicum.main_service.comments.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.events.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCreatorId(Long creatorId, PageRequest pageRequest);

    List<Comment> findAllByEvent(Event event, PageRequest pageRequest);
}
