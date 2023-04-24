package ru.practicum.main.categories.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.repository.CategoriesRepository;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.excemptions.BadRequestException;
import ru.practicum.main.excemptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static ru.practicum.main.categories.mapper.CategoryMapper.toCategory;
import static ru.practicum.main.categories.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriesAdminServiceImpl implements CategoryAdminService {
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto, HttpServletRequest httpServletRequest) {
        Category category = toCategory(newCategoryDto);
        CategoryDto result = toCategoryDto(categoriesRepository.save(category));
        log.info("CategoriesAdminService: new category {} {} created.", result.getId(), result.getName());
        return result;
    }

    @Transactional
    @Override
    public CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto, HttpServletRequest request) {
        Category newCategory = toCategory(newCategoryDto);
        Category result = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id = " + categoryId + " not founded."));
        result.setName(newCategory.getName());
        log.info("CategoriesAdminService: category {} {} updated.", result.getId(), result.getName());
        return toCategoryDto(result);
    }

    @Transactional
    @Override
    public void delete(Long categoryId, HttpServletRequest request) {
        Optional<Event> event = eventsRepository.findByCategoryId(categoryId);
        if (event.isEmpty()) {
            categoriesRepository.deleteById(categoryId);
            log.info("CategoriesAdminService: category with id = {} deleted.", categoryId);
        } else {
            throw new BadRequestException("Event with id = " + event.get().getId() + " use this category.");
        }
    }
}
