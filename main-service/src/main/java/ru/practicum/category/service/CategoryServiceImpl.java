package ru.practicum.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exceptions.CategoryNotFoundException;
import ru.practicum.exceptions.ValidationRequestException;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository repository;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Optional<Category> category = repository.findByName(newCategoryDto.getName());

        if (category.isEmpty())
            throw new ValidationRequestException("Field: name. Error: must not be blank. Value: null");

        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        Optional<Category> category = repository.findById(catId);

        if (category.isEmpty()) throw new ValidationRequestException("Category with id=" + catId + " was not found");

        repository.delete(category.get());
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Optional<Category> categoryOptional = repository.findById(catId);

        if (categoryOptional.isEmpty())
            throw new ValidationRequestException("Category with id=" + catId + " was not found");

        Category category = categoryOptional.get();
        category.setName(newCategoryDto.getName());

        return CategoryMapper.toCategoryDto(repository.save(category));
    }

    @Override
    public List<CategoryDto> getCategory(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> page = repository.findAll(pageable);

        return page.isEmpty()
                ? List.of()
                : page.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Optional<Category> category = repository.findById(catId);

        if (category.isEmpty()) throw new CategoryNotFoundException(catId);

        return CategoryMapper.toCategoryDto(category.get());
    }
}
