package com.lcwd.elestronic.store.Controller;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.elestronic.store.Dto.ApiResponseMsg;
import com.lcwd.elestronic.store.Dto.CategoryDto;
import com.lcwd.elestronic.store.Dto.ImageResponse;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.ProductDto;
import com.lcwd.elestronic.store.Services.CategoryService;
import com.lcwd.elestronic.store.Services.FileService;
import com.lcwd.elestronic.store.Services.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/categories")
@Tag(name = "CategoryController",description = " APIs for related to perform product Category operation !!")
public class CategoryController {

	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${category.image.path}")
	private String imageUploadPah;
	
	@Autowired
	private ProductService productService;
	
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	//create 
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
		
	CategoryDto categoryDto2 = categoryService.create(categoryDto);
	
	return new ResponseEntity<>(categoryDto2,HttpStatus.CREATED);
	}
	//update
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
		CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
		
		return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
	}
	
	//delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMsg> deleteCategory(@PathVariable String categoryId){
		
		categoryService.delete(categoryId);
		
		ApiResponseMsg apiResponseMsg = new ApiResponseMsg();
		apiResponseMsg.setMessage("category is deleted successfully !!");
		apiResponseMsg.setStatus(HttpStatus.OK);
		apiResponseMsg.setSuccess(true);
		
		return new ResponseEntity<>(apiResponseMsg, HttpStatus.OK);
	}
	
	//get all
	
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
			@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc",required = false) String sortDir
			
			){
		PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<>(allCategory,HttpStatus.OK);
	}
	
	//get single
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId){
		
		CategoryDto singleById = categoryService.getSingleById(categoryId);
		
		return new ResponseEntity<>(singleById,HttpStatus.OK);
	}
	
	
	//upload cover image
	
	@PostMapping("/cover/{categoryId}")
	public ResponseEntity<ImageResponse> uploadCoverImage(
			@RequestParam("coverimage") MultipartFile image,
			@PathVariable String categoryId
			) throws IOException{
		
		String imageName = fileService.uploadImageFile(image, imageUploadPah);
		
		CategoryDto category = categoryService.getSingleById(categoryId);
	    
		category.setCoverImage(imageName);
		
		CategoryDto updatedCategory = categoryService.update(category, categoryId);
		
		logger.info("updated category : {} ",updatedCategory);
		
		ImageResponse imageResponse = new ImageResponse();
		imageResponse.setImageName(imageName);
		imageResponse.setMessage("image is saved");
		imageResponse.setStatus(HttpStatus.CREATED);
		imageResponse.setSuccess(true);
		
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
	}
	
	
	//serve user image
	
	@GetMapping("/cover/{categoryId}")
	public void serveImage(@PathVariable String categoryId,HttpServletResponse response) throws IOException {
		
		//
		CategoryDto category = categoryService.getSingleById(categoryId);
		
		logger.info("category image name : {} ",category.getCoverImage());
		
		InputStream resorce = fileService.getResorce(imageUploadPah, category.getCoverImage());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resorce, response.getOutputStream());
		
	}
	
	
	
	
	//create product with category
	
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(
			@PathVariable("categoryId") String categoryId ,
			@RequestBody ProductDto productDto
			){
		
		ProductDto productwithCategory = productService.createWithCategory(productDto, categoryId);
		
		return new ResponseEntity<ProductDto>(productwithCategory,HttpStatus.CREATED);
	}
	
	
	// update category of product
	
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ProductDto> updateCategoryofProduct(
			@PathVariable String categoryId,
			@PathVariable String productId
			){
		ProductDto updateCategoryOfProduct = productService.updateCategoryOfProduct(productId, categoryId);
		return new ResponseEntity<ProductDto>(updateCategoryOfProduct,HttpStatus.OK);
	}
	
	
	//get Products of categories
	
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(
			@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc",required = false) String sortDir
			
			){
		
		PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
		
		return new ResponseEntity<PageableResponse<ProductDto>>(response,HttpStatus.OK);
		
	}
	
}
