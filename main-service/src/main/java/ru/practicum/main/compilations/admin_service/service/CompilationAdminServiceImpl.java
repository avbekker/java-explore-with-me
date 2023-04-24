package ru.practicum.main.compilations.admin_service.service;

import org.springframework.stereotype.Service;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

@Service
public class CompilationAdminServiceImpl implements CompilationAdminService {


    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public CompilationDto update(Long compilationId, UpdateCompilationRequest compilation) {
        return null;
    }

    @Override
    public void delete(Long compilationId) {

    }
}
