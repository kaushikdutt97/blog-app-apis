package com.blog.controllers;

import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.CategoryDto;
import com.blog.payloads.UserDto;
import com.blog.services.CategoryService;
import com.blog.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    CategoryService categoryService;
    @InjectMocks
    CategoryController categoryController;
    CategoryDto categoryDto;
    CategoryDto categoryDto1;

    int categoryId;
    List<CategoryDto> catDtos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryId = 1;
        categoryDto = new CategoryDto();
        categoryDto.setCategoryId(1);
        categoryDto.setCategoryTitle("Category1");
        categoryDto.setCategoryDescription("Category1 description");

        categoryDto1 = new CategoryDto();
        categoryDto1.setCategoryId(2);
        categoryDto1.setCategoryTitle("Category2");
        categoryDto1.setCategoryDescription("Category2 description");

        catDtos = new ArrayList<>();
        catDtos.add(categoryDto);
        catDtos.add(categoryDto1);
    }

    @Test
    void createCategory() {

        when(categoryService.createCategory(categoryDto)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.createCategory(categoryDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Category1", response.getBody().getCategoryTitle());

    }

    @Test
    void createCategory_notCreated(){
        categoryDto.setCategoryTitle("");

        when(categoryService.createCategory(categoryDto)).thenThrow(
                new NullPointerException("Title is null")
        );

        assertThrows(NullPointerException.class, () ->
                categoryController.createCategory(categoryDto));
    }

    @Test
    void updateCategory() {
        int categoryId = 1;
        when(categoryService.updateCategory(categoryDto, categoryId)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.updateCategory(categoryDto,
                categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category1", response.getBody().getCategoryTitle());
    }

    @Test
    void updateCategory_notUpdated(){
        categoryDto.setCategoryDescription("");

        when(categoryService.updateCategory(categoryDto, categoryId)).
                thenThrow(new NullPointerException("Description is null"));

        assertThrows(NullPointerException.class, () ->
                categoryController.updateCategory(categoryDto, categoryId));
    }

    @Test
    void updateCategory_notFound(){

        categoryId = 5;

        when(categoryService.updateCategory(categoryDto, categoryId)).
                thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryController.updateCategory(categoryDto, categoryId));
    }

    @Test
    void deleteCategory() {
        int categoryId = 1;
        doNothing().when(categoryService).deleteCategory(categoryId);

        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("category is deleted successfully !!",
                response.getBody().getMessage());
        assertEquals(true, response.getBody().isSuccess());
    }

    @Test
    void deleteCategory_notFound(){
        categoryId = 2;

        doThrow(ResourceNotFoundException.class).
                when(categoryService).deleteCategory(categoryId);

        assertThrows(ResourceNotFoundException.class, () ->
                categoryController.deleteCategory(categoryId));
    }

    @Test
    void getCategories() {

        when(categoryService.getCategories()).thenReturn(catDtos);

        ResponseEntity<List<CategoryDto>> response = categoryController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(catDtos.get(0).getCategoryTitle(),
                response.getBody().get(0).getCategoryTitle());
    }

    @Test
    void getCategories_notReturned(){

        when(categoryService.getCategories()).thenReturn(new ArrayList<>());

        ResponseEntity<List<CategoryDto>> response =
                categoryController.getCategories();

        assertEquals(0, response.getBody().size());

    }

    @Test
    void getCategory() {

        int catId = 1;

        when(categoryService.getCategory(catId)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.getCategory(catId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category1", response.getBody().getCategoryTitle());

    }

    @Test
    void getCategory_notFound(){

        categoryId = 5;

        when(categoryService.getCategory(categoryId)).
                thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryController.getCategory(categoryId));
    }
}