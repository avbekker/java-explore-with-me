package ru.practicum.main_service.categories.services.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.services.admin_service.service.CategoryAdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryAdminController {
    private final CategoryAdminService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        log.info("CategoryAdminController: POST request received for new category {}.", newCategoryDto.getName());
        return new ResponseEntity<>(service.create(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@PathVariable(value = "catId") Long categoryId,
                                              @RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("CategoryAdminController: PATCH request received for category {}.", newCategoryDto.getName());
        return ResponseEntity.ok(service.update(categoryId, newCategoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable(value = "catId") Long categoryId) {
        log.info("CategoryAdminController: DELETE request received for category with id = {}.", categoryId);
        service.delete(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
