package ru.practicum.main_service.categories.services.admin_service.service;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(Long categoryId, NewCategoryDto categoryDto);

    void delete(Long categoryId);
}
