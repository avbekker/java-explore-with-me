package ru.practicum.main_service.comments.services.private_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.NewCommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.comments.repository.CommentRepository;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.CommentException;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main_service.comments.mapper.CommentMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventsRepository eventRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto commentDto) {
        User creator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new CommentException("Comment can not be posted on event which is cancelled or in pending status.");
        }
        Comment comment = toComment(commentDto, creator, event);
        CommentDto result = toCommentDto(commentRepository.save(comment));
        log.info("CommentPrivateServiceImpl: Created new comment by user id = {} for event id = {}", userId, eventId);
        return result;
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found."));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
        if (!comment.getCreator().getId().equals(userId)) {
            throw new CommentException("Only creator can update comment.");
        }
        if (comment.getCreated().isBefore(LocalDateTime.now().minusMinutes(15))) {
            throw new CommentException("Comment cannot be updated later than 15 minutes after posting.");
        }
        comment.setMessage(commentDto.getMessage());
        log.info("CommentPrivateServiceImpl: Updated comment id = {} by user id = {}", commentId, userId);
        return toCommentDto(comment);
    }

    @Override
    public CommentDto getById(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found."));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
        if (!comment.getCreator().getId().equals(userId)) {
            throw new CommentException("Comment did not created by this user.");
        }
        log.info("CommentPrivateServiceImpl: Get comment id = {} by user id = {}", commentId, userId);
        return toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAll(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
        List<Comment> comments = commentRepository.findAllByCreatorId(userId, PageRequest.of(from / size, size));
        if (comments == null || comments.isEmpty()) {
            return List.of();
        }
        log.info("CommentPrivateServiceImpl: GET request received for all comments by user id = {}.", userId);
        return toCommentDtoList(comments);
    }
}
