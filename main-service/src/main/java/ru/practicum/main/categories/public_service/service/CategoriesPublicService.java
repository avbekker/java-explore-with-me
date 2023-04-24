package ru.practicum.main.categories.public_service.service;

import ru.practicum.main.categories.dto.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {
    List<CategoryDto> getAll(int page, int size);

    CategoryDto getById(Long categoryId);
}
