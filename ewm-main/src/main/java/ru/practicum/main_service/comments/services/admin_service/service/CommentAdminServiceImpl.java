package ru.practicum.main_service.comments.services.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.comments.repository.CommentRepository;
import ru.practicum.main_service.excemptions.NotFoundException;
import ru.practicum.main_service.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.main_service.comments.mapper.CommentMapper.toCommentDtoList;

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
        log.info("CommentAdminServiceImpl: GET request received for all comments by user id = {}.", userId);
        return toCommentDtoList(comments);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found."));
        commentRepository.deleteById(commentId);
        log.info("CommentAdminServiceImpl: Comment id = {} deleted.", commentId);
    }
}
