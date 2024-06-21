package com.lcwd.elestronic.store.Services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Dto.UserDto;
import com.lcwd.elestronic.store.Entities.User;


public interface UserService {

  public UserDto createUser(UserDto userDto);
  
  public UserDto updateUser(UserDto userDto, String userId);
  
  public void deleteUser(String userId);
  
  public PageableResponse<UserDto> getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir);
  
  public UserDto getUserById(String userId);
  
  public UserDto getUserByEmail(String email);
  
  public List<UserDto> searchUser(String keyword);
  
  Optional<User> findGoogleUserByEmail(String email);
  
}
