package ru.practicum.main_service.categories.services.public_service.service;

import ru.practicum.main_service.categories.dto.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {
    List<CategoryDto> getAll(int page, int size);

    CategoryDto getById(Long categoryId);
}
