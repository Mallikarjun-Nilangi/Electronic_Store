package com.lcwd.elestronic.store.Services;

import com.lcwd.elestronic.store.Dto.CategoryDto;
import com.lcwd.elestronic.store.Dto.PageableResponse;

public interface CategoryService {

	//create 
	
	CategoryDto create(CategoryDto categoryDto);
	
	//update
	
	CategoryDto update(CategoryDto categoryDto, String categoryId);
	
	//delete
	
	void delete(String categoryId);
	
	//get all
	
	PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	//get single category
	
	CategoryDto getSingleById(String categoryId);
	
	//search
	
	
}
