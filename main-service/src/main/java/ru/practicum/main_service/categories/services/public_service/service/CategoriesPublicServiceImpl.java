package ru.practicum.main_service.categories.services.public_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.excemptions.NotFoundException;

import java.util.List;

import static ru.practicum.main_service.categories.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.main_service.categories.mapper.CategoryMapper.toCategoryDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoriesPublicServiceImpl implements CategoriesPublicService {
    private final CategoriesRepository repository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        List<Category> categories = repository.findAll(PageRequest.of(from / size, size)).getContent();
        log.info("CategoriesPublicServiceImpl: Get all categories from {} size {}.", from, size);
        return toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id = " + categoryId + " not found."));
        log.info("CategoriesPublicServiceImpl: Get category with id = {}.", categoryId);
        return toCategoryDto(category);
    }
}
