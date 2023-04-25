package ru.practicum.main_service.categories.mapper;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    private CategoryMapper() {
    }
}
