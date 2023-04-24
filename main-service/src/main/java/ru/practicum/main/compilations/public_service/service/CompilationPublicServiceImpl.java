package ru.practicum.main.compilations.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.clients.StatisticsClient;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.compilations.repository.CompilationRepository;
import ru.practicum.main.requests.repository.RequestsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).getContent();


        return null;
    }

    @Override
    public CompilationDto getById(Long id) {
        return null;
    }
}
