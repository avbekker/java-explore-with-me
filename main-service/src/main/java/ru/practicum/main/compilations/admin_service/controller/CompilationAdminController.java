package ru.practicum.main.compilations.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.admin_service.service.CompilationAdminService;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

@Controller
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {
    private final CompilationAdminService service;

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        log.info("CompilationAdminController: POST request received for new compilation {}.", newCompilationDto.getTitle());
        return new ResponseEntity<>(service.create(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> update(@PathVariable(value = "compId") Long compilationId,
                                         @Validated @RequestBody UpdateCompilationRequest compilation) {
        log.info("CompilationAdminController: PATCH request received for compilation {}.", compilation.getTitle());
        return new ResponseEntity<>(service.update(compilationId, compilation), HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> delete(@PathVariable(value = "compId") Long compilationId) {
        log.info("CompilationAdminController: DELETE request received for compilation with id = {}.", compilationId);
        service.delete(compilationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
