package ru.practicum.main.categories.public_service.controller;

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
import ru.practicum.main.categories.public_service.service.CategoriesPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("categories")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CategoriesPublicController {
    private final CategoriesPublicService service;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("CategoriesPublicController: GET request received for all categories from {} and size {}.", from, size);
        return new ResponseEntity<>(service.getAll(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getById(@PathVariable(value = "catId") Long categoryId) {
        log.info("CategoriesPublicController: GET request received for category with id = {}", categoryId);
        return new ResponseEntity<>(service.getById(categoryId), HttpStatus.OK);
    }
}
