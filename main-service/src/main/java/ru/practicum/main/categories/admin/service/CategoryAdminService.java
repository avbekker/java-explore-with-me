package ru.practicum.main.categories.admin.service;

import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;

import javax.servlet.http.HttpServletRequest;

public interface CategoryAdminService {
    CategoryDto create(NewCategoryDto newCategoryDto, HttpServletRequest httpServletRequest);

    CategoryDto update(Long categoryId, NewCategoryDto categoryDto, HttpServletRequest request);

    void delete(Long categoryId, HttpServletRequest request);
}
