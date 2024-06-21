package com.lcwd.elestronic.store.Services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.elestronic.store.Dto.CategoryDto;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Entities.Category;
import com.lcwd.elestronic.store.Exceptions.ResourceNotFoundException;
import com.lcwd.elestronic.store.Helper.Helper;
import com.lcwd.elestronic.store.Repository.CategoryRepository;
import com.lcwd.elestronic.store.Services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class); 
	
	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		
		//creating category id randomly
		
		String categoryId = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		
		Category category = mapper.map(categoryDto, Category.class);
		Category savedCategory = categoryRepository.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
	
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {
		
		//get category of given id
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception with given id!!"));
		
		//update category details
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		
		Category savecategory = categoryRepository.save(category);
		return mapper.map(savecategory, categoryDto.getClass());
	}

	@Override
	public void delete(String categoryId) {
		
		//get category of given id

		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception with given id!!"));
		
		//delete cover image
       String fullPath = imagePath + category.getCoverImage();
		
		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
			
		} catch (NoSuchFileException e) {
			
			logger.info("user image not found in folder : ");
			e.printStackTrace();
		 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		categoryRepository.delete(category);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Category> page = categoryRepository.findAll(pageable);
		
		PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
		
		return pageableResponse;
	}

	@Override
	public CategoryDto getSingleById(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception with given id !!"));
		
		return mapper.map(category, CategoryDto.class);
	}

}
