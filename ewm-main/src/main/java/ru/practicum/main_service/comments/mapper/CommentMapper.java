package ru.practicum.main_service.comments.mapper;

import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.NewCommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main_service.users.mapper.UserMapper.toUserShortDto;

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

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .creator(toUserShortDto(comment.getCreator()))
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
