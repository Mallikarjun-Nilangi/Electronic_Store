package com.lcwd.elestronic.store.Dto;

import java.util.HashSet;
import java.util.Set;

import com.lcwd.elestronic.store.Entities.Role;
import com.lcwd.elestronic.store.validate.ImageNameValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

	private String userId;
	
	@Size(min=3,max=25,message = "Invalid Name!")
    @Schema( name = "username", accessMode = Schema.AccessMode.READ_ONLY, description = "user name of new user !!")
	private String name;
	
	//@Email(message = "Invalid User Email! ")
	@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !")
	@NotBlank(message = "Email is required !!")
	private String email;
	
	@NotBlank(message = "Password is required !!")
	private String password;
	
	
	private String about;
	
	@NotBlank(message = "write something about !")
	private String gender;
	
	@ImageNameValid(message = "Invalid Image Name")
	private String imageName;
	
	private Set<RoleDto> roles = new HashSet<>();
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	
	
	
	
	public UserDto(String userId, @Size(min = 3, max = 25, message = "Invalid Name!") String name,
			@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !") @NotBlank(message = "Email is required !!") String email,
			@NotBlank(message = "Password is required !!") String password, String about,
			@NotBlank(message = "write something about !") String gender, String imageName, Set<RoleDto> roles) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.about = about;
		this.gender = gender;
		this.imageName = imageName;
		this.roles = roles;
	}

	public Set<RoleDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDto> roles) {
		this.roles = roles;
	}

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
