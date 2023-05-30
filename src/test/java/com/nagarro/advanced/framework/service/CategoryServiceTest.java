package com.nagarro.advanced.framework.service;


import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.persistence.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final String NO_NAME_ERROR = "There is no category named ";
    private static final String NO_UUID_DELETE_ERROR = "There is no category with this uuid to be deleted";
    private static final String NO_UUID_UPDATE_ERROR = "There is no category with this uuid to be updated";
    private static final String NO_UUID_GET_ERROR = "There is no category with this uuid to be used";
    private static final String NAME_EXISTS_ERROR = "This name already exists";

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldSaveCategory() {
        //given
        Category expectedCategory = Category.builder().name("romance").build();

        //when
        when(categoryRepository.findByName(expectedCategory.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(expectedCategory)).thenReturn(expectedCategory);
        Category actualCategory = categoryService.createCategory(expectedCategory);

        //then
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void shouldThrowExceptionWhenSavingCategoryWithExistingName() {
        //given
        Category invalidCategory = Category.builder().build();

        //when
        when(categoryRepository.findByName(invalidCategory.getName())).thenReturn(Optional.of(invalidCategory));

        //then
        AppException exception = assertThrows(AppException.class, ()
                -> categoryService.createCategory(invalidCategory));

        assertEquals(NAME_EXISTS_ERROR, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void shouldFindCategoryByUuid() {
        //given
        Category expectedCategory = Category.builder().name("comedy").build();

        //when
        when(categoryRepository.findByUuid(expectedCategory.getUuid())).thenReturn(Optional.of(expectedCategory));
        Category actualCategory = categoryService.getCategoryByUuid(expectedCategory.getUuid());

        //then
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void shouldThrowExceptionWhenFindingCategoryForInvalidUuid() {
        //given
        Category notFoundCategory = Category.builder().build();

        //when
        when(categoryRepository.findByUuid(notFoundCategory.getUuid())).thenReturn(Optional.empty());

        //then
        AppException exception = assertThrows(AppException.class, ()
                -> categoryService.getCategoryByUuid(notFoundCategory.getUuid()));
        assertEquals(NO_UUID_GET_ERROR, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void shouldFindCategoryByName() {
        //given
        Category expectedCategory = Category.builder().name("crime").build();

        //when
        when(categoryRepository.findByName(expectedCategory.getName())).thenReturn(Optional.of(expectedCategory));
        Category actualCategory = categoryService.getCategoryByName(expectedCategory.getName());

        //then
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void shouldThrowExceptionWhenFindingCategoryForInvalidName() {
        //given
        Category notFoundCategory = Category.builder().build();

        //when
        when(categoryRepository.findByName(notFoundCategory.getName())).thenReturn(Optional.empty());

        //then
        AppException exception = assertThrows(AppException.class, ()
                -> categoryService.getCategoryByName(notFoundCategory.getName()));
        assertEquals(exception.getMessage(), NO_NAME_ERROR + notFoundCategory.getName());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void shouldDeleteCategoryByUuid() {
        //given
        Category category = Category.builder().name("thriller").build();

        //when
        when(categoryRepository.findByUuid(category.getUuid())).thenReturn(Optional.of(category));
        categoryService.deleteCategory(category.getUuid());

        //then
        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldThrowExceptionWhenDeletingCategoryForInvalidUuid() {
        //given
        Category notFoundCategory = Category.builder().build();

        //when
        when(categoryRepository.findByUuid(notFoundCategory.getUuid())).thenReturn(Optional.empty());

        //then
        AppException exception = assertThrows(AppException.class, ()
                -> categoryService.deleteCategory(notFoundCategory.getUuid()));
        assertEquals(NO_UUID_DELETE_ERROR, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void shouldUpdateCategory() {
        //given
        Category toBeUpdatedCategory = Category.builder().name("crime").build();
        Category updateCategory = Category.builder().name("romance").build();

        //when
        when(categoryRepository.findByUuid(toBeUpdatedCategory.getUuid())).thenReturn(Optional.of(toBeUpdatedCategory));
        when(categoryRepository.findByName(updateCategory.getName())).thenReturn(Optional.empty());
        categoryService.updateCategory(toBeUpdatedCategory.getUuid(), updateCategory);

        //then
        assertEquals(toBeUpdatedCategory.getName(), updateCategory.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingCategoryForInvalidUuid() {
        //given
        Category notFoundCategory = Category.builder().build();
        Category updateCategory = Category.builder().build();

        //when
        when(categoryRepository.findByUuid(notFoundCategory.getUuid())).thenReturn(Optional.empty());

        //then
        AppException exception = assertThrows(AppException.class, ()
                -> categoryService.updateCategory(notFoundCategory.getUuid(), updateCategory));
        assertEquals(NO_UUID_UPDATE_ERROR, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingCategoryForNewInvalidName() {
        //given
        Category toBeUpdatedCategory = Category.builder().build();
        Category updateCategory = Category.builder().build();

        //when
        when(categoryRepository.findByUuid(toBeUpdatedCategory.getUuid())).thenReturn(Optional.of(toBeUpdatedCategory));
        when(categoryRepository.findByName(updateCategory.getName())).thenReturn(Optional.of(updateCategory));

        //then
        AppException exception = assertThrows(AppException.class, ()
                -> categoryService.updateCategory(toBeUpdatedCategory.getUuid(), updateCategory));
        assertEquals(NAME_EXISTS_ERROR, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

}
