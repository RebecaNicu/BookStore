package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.CategoryDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryFacade {
    private final CategoryService categoryService;

    private final Converter<Category, CategoryDto> categoryConverter;

    @Autowired
    public CategoryFacade(CategoryService categoryService, Converter<Category, CategoryDto> categoryConverter) {
        this.categoryService = categoryService;
        this.categoryConverter = categoryConverter;
    }

    public CategoryDto saveCategory(CategoryDto categorySaveDto) {
        Category category = categoryConverter.toEntity(categorySaveDto);
        return categoryConverter.toDto(categoryService.createCategory(category));
    }

    public CategoryDto findCategoryByUuid(String uuid) {
        Category category = categoryService.getCategoryByUuid(uuid);
        return categoryConverter.toDto(category);
    }

    public CategoryDto findCategoryByName(String name) {
        Category category = categoryService.getCategoryByName(name);
        return categoryConverter.toDto(category);
    }

    public void deleteCategoryByUuid(String uuid) {
        categoryService.deleteCategory(uuid);
    }

    public void updateCategory(String uuid, CategoryDto categoryUpdateDto) {
        categoryService.updateCategory(uuid, categoryConverter.toEntity(categoryUpdateDto));
    }
}
