package com.lcwd.elestronic.store.Controller;

import java.io.IOException;

import java.io.InputStream;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.elestronic.store.Dto.ApiResponseMsg;
import com.lcwd.elestronic.store.Dto.ImageResponse;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.UserDto;
import com.lcwd.elestronic.store.Services.FileService;
import com.lcwd.elestronic.store.Services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "UserController",description = "REST APIs related to perform user operations !!")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPah;
	
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping
	@Operation(summary = "create new user !!", description = "this is user api")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK"),
            @ApiResponse(responseCode = "401", description = "not authorized !!"),
            @ApiResponse(responseCode = "201", description = "new user created !!")
    })
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto user = userService.createUser(userDto);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId, @RequestBody UserDto userDto) {

		UserDto updateUser = userService.updateUser(userDto, userId);
		return new ResponseEntity<>(updateUser, HttpStatus.OK);

	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMsg> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		ApiResponseMsg apiResponseMsg = new ApiResponseMsg();
		apiResponseMsg.setMessage("User is deleted successfully !");
		apiResponseMsg.setStatus(HttpStatus.OK);
		apiResponseMsg.setSuccess(true);
		return new ResponseEntity<>(apiResponseMsg, HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "get all users")
	public ResponseEntity<PageableResponse<UserDto>> getAllUser(
			 @RequestParam(value = "pageNumber", defaultValue = "1",required = false) int pageNumber,
			 @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			 @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
			 @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
			) {
		return new ResponseEntity<>(userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		
		return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
	}
	
	@GetMapping("search/{keyword}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword){
		return new ResponseEntity<>(userService.searchUser(keyword),HttpStatus.OK);
	}
	
	
	//upload user image
	
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(
			@RequestParam("userImage") MultipartFile image,
			@PathVariable String userId
			) throws IOException{
		
		String imageName = fileService.uploadImageFile(image, imageUploadPah);
		
		UserDto user = userService.getUserById(userId);
	    
		user.setImageName(imageName);
		
		UserDto updatedUserDto = userService.updateUser(user, userId);
		
		logger.info("updated user : {} ",updatedUserDto);
		
		ImageResponse imageResponse = new ImageResponse();
		imageResponse.setImageName(imageName);
		imageResponse.setMessage("image is saved");
		imageResponse.setStatus(HttpStatus.CREATED);
		imageResponse.setSuccess(true);
		
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
	}
	
	
	//serve user image
	
	@GetMapping("/image/{userId}")
	public void serveImage(@PathVariable String userId,HttpServletResponse response) throws IOException {
		
		//
		UserDto user = userService.getUserById(userId);
		
		logger.info("user image name : {} ",user.getImageName());
		
		InputStream resorce = fileService.getResorce(imageUploadPah, user.getImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resorce, response.getOutputStream());
		
	}
}
