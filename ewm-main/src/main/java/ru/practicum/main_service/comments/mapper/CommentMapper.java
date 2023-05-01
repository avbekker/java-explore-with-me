package ru.practicum.main_service.comments.mapper;

import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.NewCommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.users.dto.UserShortDto;
import ru.practicum.main_service.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentMapper {
    private CommentMapper() {
    }

    public static Comment toComment(NewCommentDto newCommentDto, User creator, Event event) {
        return Comment.builder()
                .id(null)
                .message(newCommentDto.getMessage())
                .creator(creator)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment, UserShortDto creator, Long eventId) {
        return CommentDto.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .creator(creator)
                .eventId(eventId)
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> comments, Map<Long, UserShortDto> creators,
                                                    Map<Long, Long> eventsIds) {
        return comments.stream()
                .map(comment -> toCommentDto(comment, creators.get(comment.getId()), eventsIds.get(comment.getId())))
                .collect(Collectors.toList());
    }
}
