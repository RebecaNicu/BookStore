package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService{

    private static final String NO_NAME_ERROR = "There is no category named ";
    private static final String NO_UUID_DELETE_ERROR = "There is no category with this uuid to be deleted";
    private static final String NO_UUID_UPDATE_ERROR = "There is no category with this uuid to be updated";
    private static final String NO_UUID_GET_ERROR = "There is no category with this uuid to be used";
    private static final String NAME_EXISTS_ERROR = "This name already exists";
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
        if (optionalCategory.isPresent()) {
            throw new AppException(NAME_EXISTS_ERROR, HttpStatus.BAD_REQUEST);
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(String uuid) {
        Optional<Category> optionalCategory = categoryRepository.findByUuid(uuid);
        if (optionalCategory.isEmpty()) {
            throw new AppException(NO_UUID_DELETE_ERROR, HttpStatus.NOT_FOUND);
        }
        categoryRepository.delete(optionalCategory.get());
    }

    public Category getCategoryByUuid(String uuid) {
        Optional<Category> optionalCategory = categoryRepository.findByUuid(uuid);
        if (optionalCategory.isEmpty()) {
            throw new AppException(NO_UUID_GET_ERROR, HttpStatus.NOT_FOUND);
        }
        return optionalCategory.get();
    }

    public Category getCategoryByName(String name) {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);
        if (categoryOptional.isEmpty()) {
            throw new AppException(NO_NAME_ERROR + name, HttpStatus.NOT_FOUND);
        }
        return categoryOptional.get();
    }

    public void updateCategory(String uuid, Category category) {
        Optional<Category> optionalCategory = categoryRepository.findByUuid(uuid);
        if (optionalCategory.isEmpty()) {
            throw new AppException(NO_UUID_UPDATE_ERROR, HttpStatus.NOT_FOUND);
        }
        Optional<Category> optional = categoryRepository.findByName(category.getName());
        if (optional.isPresent()) {
            throw new AppException(NAME_EXISTS_ERROR, HttpStatus.BAD_REQUEST);
        }
        Category toSetCategory = optionalCategory.get();
        toSetCategory.setName(category.getName());
    }
}
