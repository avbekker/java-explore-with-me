package ru.practicum.main_service.compilations.services.admin_service.service;

import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.dto.UpdateCompilationRequest;

public interface CompilationAdminService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto update(Long compilationId, UpdateCompilationRequest compilation);

    void delete(Long compilationId);
}
