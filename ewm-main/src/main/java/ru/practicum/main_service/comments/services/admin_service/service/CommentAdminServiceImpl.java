package ru.practicum.main_service.comments.services.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.comments.repository.CommentRepository;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.users.dto.UserShortDto;
import ru.practicum.main_service.users.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.main_service.comments.mapper.CommentMapper.toCommentDtoList;
import static ru.practicum.main_service.users.mapper.UserMapper.toUserShortDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentDto> getAllByUser(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
        List<Comment> comments = commentRepository.findAllByCreatorId(userId, PageRequest.of(from / size, size));
        if (comments == null || comments.isEmpty()) {
            return List.of();
        }
        Map<Long, UserShortDto> creators = getCreatorsByComments(comments);
        Map<Long, Long> events = getEventsByComments(comments);
        log.info("CommentAdminServiceImpl: GET request received for all comment by user id = {}.", userId);
        return toCommentDtoList(comments, creators, events);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found."));
        commentRepository.deleteById(commentId);
        log.info("CommentAdminServiceImpl: Comment id = {} deleted.", commentId);
    }

    private Map<Long, Long> getEventsByComments(List<Comment> comments) {
        return comments.stream().collect(Collectors.toMap(Comment::getId, comment -> comment.getEvent().getId()));
    }

    private Map<Long, UserShortDto> getCreatorsByComments(List<Comment> comments) {
        return comments.stream().collect(Collectors.toMap(Comment::getId, comment -> toUserShortDto(comment.getCreator())));
    }
}
