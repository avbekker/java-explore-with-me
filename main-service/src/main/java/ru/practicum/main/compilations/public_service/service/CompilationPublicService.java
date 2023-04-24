package ru.practicum.main.compilations.public_service.service;

import ru.practicum.main.compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getAll(Boolean pinned, Integer page, Integer size);

    CompilationDto getById(Long id);
}
