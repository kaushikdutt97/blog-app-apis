package com.blog.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.blog.entities.User;
import com.blog.payloads.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDto;
import com.blog.repositories.CategoryRepo;
import com.blog.services.CategoryService;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	public Category dtoToCategory(CategoryDto categoryDto) {
		log.debug("Converting CategoryDto to Category");

		Category category = this.modelMapper.map(categoryDto, Category.class);
		return category;
	}

	public CategoryDto categoryToDto(Category category) {
		log.debug("Converting Category to CategoryDto");

		CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
		return categoryDto;
	}

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		log.info("Creating category: {}", categoryDto);

		Category cat = this.dtoToCategory(categoryDto);
		Category addedCat = this.categoryRepo.save(cat);
		return this.categoryToDto(addedCat);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

		log.info("Updating category with ID {}: {}", categoryId, categoryDto);

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category ",
						"Category Id", categoryId));

		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDescription(categoryDto.getCategoryDescription());

		Category updatedCategory = this.categoryRepo.save(cat);

		return this.categoryToDto(updatedCategory);
	}

	@Override
	public void deleteCategory(Integer categoryId) {

		log.info("Deleting category with ID {}", categoryId);

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> {
					String errMsg = String.format("Category not found with id %d ", categoryId);
					log.error(errMsg);
					return new ResourceNotFoundException("Category", "category id", categoryId);
				}
				);

		this.categoryRepo.delete(cat);
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {

		log.info("Retrieving category with ID {}", categoryId);

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> {
					String errMsg = String.format("Category not found with id %d ", categoryId);
					log.error(errMsg);
					return new ResourceNotFoundException("Category", "category id", categoryId);
				}
				);

		return this.categoryToDto(cat);
	}

	@Override
	public List<CategoryDto> getCategories() {

		log.info("Retrieving all categories");

		List<Category> categories = this.categoryRepo.findAll();
//		List<CategoryDto> catDtos = this.categoriesToDtos(categories);
		List<CategoryDto> categoryDtos = new ArrayList<>();


		for(Category category: categories){
			CategoryDto categoryDto = new CategoryDto(category.getCategoryId(),
					category.getCategoryTitle(), category.getCategoryDescription());
			categoryDtos.add(categoryDto);
		}

		return categoryDtos;
	}

//	public List<CategoryDto> categoriesToDtos(List<Category> categories){
//		List<CategoryDto> catDtos = categories.stream().map((cat) ->
//						this.modelMapper.map(cat, CategoryDto.class))
//				.collect(Collectors.toList());
//
//		return catDtos;
//	}
}
