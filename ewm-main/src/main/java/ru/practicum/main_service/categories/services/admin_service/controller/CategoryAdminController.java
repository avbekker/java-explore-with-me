package ru.practicum.main_service.categories.services.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.services.admin_service.service.CategoryAdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryAdminController {
    private final CategoryAdminService service;

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        log.info("CategoryAdminController: POST request received for new category {}.", newCategoryDto.getName());
        return new ResponseEntity<>(service.create(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> update(@PathVariable(value = "catId") Long categoryId,
                                         @RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("CategoryAdminController: PATCH request received for category {}.", newCategoryDto.getName());
        return new ResponseEntity<>(service.update(categoryId, newCategoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable(value = "catId") Long categoryId) {
        log.info("CategoryAdminController: DELETE request received for category with id = {}.", categoryId);
        service.delete(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
