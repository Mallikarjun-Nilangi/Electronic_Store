package com.lcwd.elestronic.store.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CategoryDto {

	
	private String categoryId;
	

	@NotBlank(message = "title is required !")
	@Min(value = 3,message = "title must be of minimum 4 character !!")
	private String title;
	
	@NotBlank(message = "description is required !")
	private String description;
	
	@NotBlank(message = "cover image is required !")
	private String coverImage;


	public String getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCoverImage() {
		return coverImage;
	}


	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}


	public CategoryDto(String categoryId, String title, String description, String coverImage) {
		super();
		this.categoryId = categoryId;
		this.title = title;
		this.description = description;
		this.coverImage = coverImage;
	}


	public CategoryDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
