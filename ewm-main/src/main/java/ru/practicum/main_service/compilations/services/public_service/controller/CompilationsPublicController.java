package ru.practicum.main_service.compilations.services.public_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.services.public_service.service.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationsPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAll(@RequestParam(value = "pinned", defaultValue = "false") Boolean pinned,
                                                       @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("CompilationsPublicController: GET request received for all compilations from {} size {}, isPinned {}",
                from, size, pinned);
        return ResponseEntity.ok(service.getAll(pinned, from, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompilationDto> getById(@PathVariable Long id) {
        log.info("CompilationsPublicController: GET request received for compilation with id = {}", id);
        return ResponseEntity.ok(service.getById(id));
    }
}
