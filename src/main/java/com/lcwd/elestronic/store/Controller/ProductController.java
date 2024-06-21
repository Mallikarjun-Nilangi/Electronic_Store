package com.lcwd.elestronic.store.Controller;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.lcwd.elestronic.store.Dto.ImageResponse;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.ProductDto;
import com.lcwd.elestronic.store.Dto.UserDto;
import com.lcwd.elestronic.store.Services.FileService;
import com.lcwd.elestronic.store.Services.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/products")
@Tag(name = "ProductController",description = " REST APIs for related to perform Product operation !!")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Value("${product.image.path}")
	private String imageUploadPah;
	
	@Autowired
	private FileService fileService;
	
	private Logger logger = LoggerFactory.getLogger(ProductController.class);

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
		ProductDto productDto2 = productService.create(productDto);
		
		return new ResponseEntity<>(productDto2,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,@RequestBody ProductDto productDto){
		
		ProductDto updateProduct = productService.update(productDto, productId);
		
		return new ResponseEntity<>(updateProduct,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMsg> deleteProduct(@PathVariable String productId) {
		
		productService.delete(productId);
		
		ApiResponseMsg apiResponseMsg = new ApiResponseMsg();
		
		apiResponseMsg.setMessage("product is deleted successfully !!");
		apiResponseMsg.setStatus(HttpStatus.OK);
		apiResponseMsg.setSuccess(true);
		
		return new ResponseEntity<>(apiResponseMsg,HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
		ProductDto product = productService.getById(productId);
		
		return new ResponseEntity<>(product,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc",required = false) String sortDir
			
			){
		
		PageableResponse<ProductDto> response = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/AllLiveProducts")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
			@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc",required = false) String sortDir
			
			){
		
		PageableResponse<ProductDto> allLiveproducts = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<PageableResponse<ProductDto>>(allLiveproducts,HttpStatus.OK);
	}
	
	
	@GetMapping("/search/{title}")
	public ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
			@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc",required = false) String sortDir,
			@PathVariable String title
			){
		
		PageableResponse<ProductDto> searchByTitle = productService.searchByTitle(title, pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<PageableResponse<ProductDto>>(searchByTitle,HttpStatus.OK);
		
	}
	
	//product image upload
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(
			@RequestParam("productImage") MultipartFile image,
			@PathVariable String productId
			) throws IOException{
		
		String imageName = fileService.uploadImageFile(image, imageUploadPah);
		
		ProductDto productDto = productService.getById(productId);
	    
		productDto.setProductImageName(imageName);
		
		ProductDto update = productService.update(productDto, productId);
		
		ImageResponse imageResponse = new ImageResponse();
		imageResponse.setImageName(imageName);
		imageResponse.setMessage("image is saved");
		imageResponse.setStatus(HttpStatus.CREATED);
		imageResponse.setSuccess(true);
		
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
	}
	
	
	//serve user image
	
	@GetMapping("/image/{productId}")
	public void serveImage(@PathVariable String productId,HttpServletResponse response) throws IOException {
		
		//
		ProductDto productDto = productService.getById(productId);
		
		logger.info("user image name : {} ",productDto.getProductImageName());
		
		InputStream resorce = fileService.getResorce(imageUploadPah, productDto.getProductImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resorce, response.getOutputStream());
		
	}
	
	
	
	
}
