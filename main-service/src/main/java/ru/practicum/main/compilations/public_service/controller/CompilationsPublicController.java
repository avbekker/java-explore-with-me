package ru.practicum.main.compilations.public_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main.compilations.public_service.service.CompilationPublicService;

import javax.validation.constraints.Min;

@Controller
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationsPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(value = "pinned", defaultValue = "false") Boolean pinned,
                                         @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                         @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("CompilationsPublicController: GET request received for all compilations from {} size {}, isPinned {}",
                from, size, pinned);
        return new ResponseEntity<>(service.getAll(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("CompilationsPublicController: GET request received for compilation with id = {}", id);
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }
}
