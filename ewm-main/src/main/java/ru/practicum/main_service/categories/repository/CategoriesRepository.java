package ru.practicum.main_service.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.categories.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
