package com.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.CategoryDto;
import com.blog.services.CategoryService;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// create

	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto cateogDto) {

		log.info("Creating category: {}", cateogDto);

		CategoryDto createCategory = this.categoryService.createCategory(cateogDto);

		log.info("Category created successfully: {}", createCategory);

		return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
	}


	// update

	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable Integer catId) {

		log.info("Updating category with ID {}: {}", catId, categoryDto);

		CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, catId);

		log.info("Category updated successfully: {}", updatedCategory);

		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
	}

	// delete

	@DeleteMapping("/{catId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId) {

		log.info("Deleting category with ID: {}", catId);

		this.categoryService.deleteCategory(catId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(
				"category is deleted successfully !!", true), HttpStatus.OK);
	}
	// get

	@GetMapping("/{catId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {

		log.info("Fetching category with ID: {}", catId);

		CategoryDto categoryDto = this.categoryService.getCategory(catId);

		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);

	}

	// get all
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getCategories() {

		log.info("Fetching all categories");

		List<CategoryDto> categories = this.categoryService.getCategories();
		return ResponseEntity.ok(categories);
	}

}
