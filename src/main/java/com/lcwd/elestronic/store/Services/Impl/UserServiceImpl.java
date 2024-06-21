package com.lcwd.elestronic.store.Services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.UserDto;
import com.lcwd.elestronic.store.Entities.Role;
import com.lcwd.elestronic.store.Entities.User;
import com.lcwd.elestronic.store.Exceptions.ResourceNotFoundException;
import com.lcwd.elestronic.store.Helper.Helper;
import com.lcwd.elestronic.store.Repository.RoleRepository;
import com.lcwd.elestronic.store.Repository.UserRepository;
import com.lcwd.elestronic.store.Services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${normal.role.id}")
	private String normalRoleId;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDto createUser(UserDto userDto) {

		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		
		//set encoding password
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		User user = DtoToEntity(userDto);
		
		//fetch role of normal and set it to user
		Role role = roleRepository.findById(normalRoleId).get();
		user.getRoles().add(role);
		
		User savedUser = userRepository.save(user);
		UserDto savedUserDto = EntityToDto(savedUser);
		
		

		return savedUserDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found exception"));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
	    user.setGender(userDto.getGender());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setImageName(userDto.getImageName());

		User updatedUser = userRepository.save(user);
		UserDto updateduserDto = EntityToDto(updatedUser);
	  //UserDto updateduserDto = modelMapper.map(updatedUser, UserDto.class);
		return updateduserDto;
	}

	@Override
	public void deleteUser(String userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found exception"));
		
		//delete user profile image
		
		String fullPath = imagePath + user.getImageName();
		
		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
			
		} catch (NoSuchFileException e) {
			
			logger.info("user image not found in folder : ");
			e.printStackTrace();
		 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// delete user
		userRepository.delete(user);
		

	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize,String sortBy,String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		Pageable pageable = PageRequest.of(pageNumber-1, pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        
        
//        List<User> users = page.getContent();
//        
//        
//		List<UserDto> dtoList = users.stream().map(user -> EntityToDto(user)).collect(Collectors.toList());
//
//		PageableResponse<UserDto> response = new PageableResponse<>();
//		response.setContent(dtoList);
//		response.setPageNumber(page.getNumber());
//		response.setPageSize(page.getSize());
//		response.setTotalElements(page.getTotalElements());
//		response.setTotalPages(page.getTotalPages());
//		response.setLastPage(page.isLast());
        
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
		
		return response;
	}

	@Override
	public UserDto getUserById(String userId) {

		User byId = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found exception"));
		return EntityToDto(byId);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user not found with given email"));

		return EntityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {

		List<User> byNameContaining = userRepository.findByNameContaining(keyword);
		return byNameContaining.stream().map(user -> EntityToDto(user)).collect(Collectors.toList());

	}

	private User DtoToEntity(UserDto userDto) {

		return modelMapper.map(userDto, User.class);
	}

	private UserDto EntityToDto(User savedUser) {
		// TODO Auto-generated method stub
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public Optional<User> findGoogleUserByEmail(String email) {
	
		return userRepository.findByEmail(email);
	}

}
