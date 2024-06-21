package com.lcwd.elestronic.store.Services;

import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.ProductDto;

public interface ProductService {
	
	//create
	
	ProductDto create(ProductDto productDto);
	
	//create with category
	
	ProductDto createWithCategory(ProductDto productDto,String categoryId);
	
	//update
	
	ProductDto update(ProductDto productDto,String productId);
	
	
	//update category of products
	
	ProductDto updateCategoryOfProduct(String productId,String categoryId);
	
	//delete
	
	void delete(String productId);
	
	//getAll
	
	PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	//getSingle
	
	ProductDto getById(String productId);
	
	
	//get All : Live
	
	PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	//search products
	
	PageableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir); 
	

	//
	PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
	
}
