package com.blog.services.impl;

import com.blog.entities.Category;
import com.blog.payloads.CategoryDto;
import com.blog.repositories.CategoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Stream stream;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    CategoryDto categoryDto;
    Category category;

    CategoryDto categoryDto2;
    Category category2;

    int categoryId;

    List<Category> categories;
    List<CategoryDto> categoryDtos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        categoryId =1;
        categoryDto = new CategoryDto();
        categoryDto.setCategoryId(categoryId);
        categoryDto.setCategoryTitle("Category1");
        categoryDto.setCategoryDescription("Category1 description");

        category = new Category();
        category.setCategoryId(categoryDto.getCategoryId());
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        categoryDto2 = new CategoryDto();
        categoryDto2.setCategoryId(2);
        categoryDto2.setCategoryTitle("Category2");
        categoryDto2.setCategoryDescription("Category2 description");

        category2 = new Category();
        category2.setCategoryId(categoryDto2.getCategoryId());
        category2.setCategoryTitle(categoryDto2.getCategoryTitle());
        category2.setCategoryDescription(categoryDto2.getCategoryDescription());

        categories = new ArrayList<>();
        categories.add(category);
        categories.add(category2);

        categoryDtos = new ArrayList<>();
        categoryDtos.add(categoryDto);
        categoryDtos.add(categoryDto2);
    }

    @Test
    void createCategory() {


        when(categoryRepo.save(category)).thenReturn(category);
        when(categoryService.categoryToDto(category)).thenReturn(categoryDto);
        when(categoryService.dtoToCategory(categoryDto)).thenReturn(category);

        CategoryDto resultCatDto = categoryService.createCategory(categoryDto);

        assertEquals(categoryDto.getCategoryId(), resultCatDto.getCategoryId());

    }

    @Test
    void updateCategory() {

        categoryId = 1;
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepo.save(category)).thenReturn(category);
        when(categoryService.categoryToDto(category)).thenReturn(categoryDto);
        when(categoryService.dtoToCategory(categoryDto)).thenReturn(category);

        CategoryDto resultCatDto = categoryService.updateCategory(categoryDto, categoryId);

        assertEquals(categoryDto.getCategoryId(), resultCatDto.getCategoryId());
    }

    @Test
    void deleteCategory() {
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepo, times(1)).delete(category);
    }

    @Test
    void getCategory() {

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryService.categoryToDto(category)).thenReturn(categoryDto);

        CategoryDto resultCatDto = categoryService.getCategory(categoryId);

        assertEquals(categoryDto.getCategoryId(), resultCatDto.getCategoryId());
    }

    @Test
    void getCategories() {

        when(categoryRepo.findAll()).thenReturn(categories);

        List<CategoryDto> resultCategoryDtos = categoryService.getCategories();

        assertEquals(categoryDtos.get(0).getCategoryTitle(),
                resultCategoryDtos.get(0).getCategoryTitle());

        assertEquals(categoryDtos.get(1).getCategoryTitle(),
                resultCategoryDtos.get(1).getCategoryTitle());
    }

}