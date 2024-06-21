package com.lcwd.elestronic.store.Services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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

import com.lcwd.elestronic.store.Dto.ApiResponseMsg;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.ProductDto;
import com.lcwd.elestronic.store.Entities.Category;
import com.lcwd.elestronic.store.Entities.Product;
import com.lcwd.elestronic.store.Exceptions.ResourceNotFoundException;
import com.lcwd.elestronic.store.Helper.Helper;
import com.lcwd.elestronic.store.Repository.CategoryRepository;
import com.lcwd.elestronic.store.Repository.ProductRepository;
import com.lcwd.elestronic.store.Services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Value("${product.image.path}")
	private String imageUploadPah;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Override
	public ProductDto create(ProductDto productDto) {
		
		// set product id
		 String productId = UUID.randomUUID().toString();
		 productDto.setProductId(productId);
		 
		 //set product added date
		 
		 productDto.setAddedDate(new Date());
		 
		Product product = mapper.map(productDto, Product.class);
		Product savedProduct = productRepository.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	
	}
	
	//create  product with category
	
	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
		
         //fetch category from db

         Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found with given id !!"));
        // set product id
		 String productId = UUID.randomUUID().toString();
		 productDto.setProductId(productId);
		 
		 //set product added date
		 
		 productDto.setAddedDate(new Date());
		 
		 
		Product product = mapper.map(productDto, Product.class);
		
		product.setCategory(category);
		
		Product savedProduct = productRepository.save(product);
		return mapper.map(savedProduct, ProductDto.class);

	}
	
	
	
	

	@Override
	public ProductDto update(ProductDto productDto, String productId) {
		
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with given id !!"));
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setProductImageName(productDto.getProductImageName());
		Product saveproduct = productRepository.save(product);
		
		return mapper.map(saveproduct, ProductDto.class);
		
	}

	@Override
	public void delete(String productId) {
		
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id !!"));

		//delete product image
		
        String fullPath = imageUploadPah + product.getProductImageName();
		
		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
			
		} catch (NoSuchFileException e) {
			
			logger.info("user image not found in folder : ");
			e.printStackTrace();
		 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		productRepository.delete(product);
	}


	@Override
	public ProductDto getById(String productId) {

		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id !!"));
         ProductDto productDto = mapper.map(product, ProductDto.class);
		return productDto;
	}
	
	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page = productRepository.findAll(pageable);
		
		return Helper.getPageableResponse(page, ProductDto.class);
		
		
	}
	

	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		
		return Helper.getPageableResponse(page, ProductDto.class);
		
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page = productRepository.findByTitleContaining(title,pageable);
		
		return Helper.getPageableResponse(page, ProductDto.class);
		
	}
	
	//update category of products

	@Override
	public ProductDto updateCategoryOfProduct(String productId, String categoryId) {
		
		//fetch product
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product of given id not found !!"));
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found with given id !!"));
		
		product.setCategory(category);
		Product savedproduct = productRepository.save(product);
		
		return mapper.map(savedproduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category not found with given id !!"));
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		
		
		Page<Product> page = productRepository.findByCategory(category,pageable);
		
		return Helper.getPageableResponse(page, ProductDto.class);
	}

	

}
