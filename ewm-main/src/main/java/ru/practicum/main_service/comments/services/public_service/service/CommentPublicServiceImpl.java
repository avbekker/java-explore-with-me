package ru.practicum.main_service.comments.services.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.repository.CommentRepository;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.excemptions.CommentException;
import ru.practicum.main_service.excemptions.NotFoundException;

import java.util.List;

import static ru.practicum.main_service.comments.mapper.CommentMapper.toCommentDtoList;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentPublicServiceImpl implements CommentPublicService {
    private final CommentRepository commentRepository;
    private final EventsRepository eventRepository;

    @Override
    public List<CommentDto> getByEvent(Long eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new CommentException("Event did not published. There are no comments.");
        }
        List<CommentDto> result = toCommentDtoList(commentRepository.findAllByEvent(event, PageRequest.of(from / size, size)));
        log.info("CommentPublicServiceImpl: Get all comments of event id = {}.", eventId);
        return result;
    }
}
