package ru.practicum.main_service.categories.services.admin_service.service;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;

import javax.servlet.http.HttpServletRequest;

public interface CategoryAdminService {
    CategoryDto create(NewCategoryDto newCategoryDto, HttpServletRequest httpServletRequest);

    CategoryDto update(Long categoryId, NewCategoryDto categoryDto, HttpServletRequest request);

    void delete(Long categoryId, HttpServletRequest request);
}
