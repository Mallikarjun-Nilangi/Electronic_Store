package com.lcwd.elestronic.store.Dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDto {

	private String cartId;
	
	private Date createdDate;
	
	private UserDto user;
	
	
	//mapping cart-item
	
	private List<CartItemDto> items = new ArrayList<>();


	public String getCartId() {
		return cartId;
	}


	public void setCartId(String cartId) {
		this.cartId = cartId;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public UserDto getUser() {
		return user;
	}


	public void setUser(UserDto user) {
		this.user = user;
	}


	public List<CartItemDto> getItems() {
		return items;
	}


	public void setItems(List<CartItemDto> items) {
		this.items = items;
	}


	public CartDto(String cartId, Date createdDate, UserDto user, List<CartItemDto> items) {
		super();
		this.cartId = cartId;
		this.createdDate = createdDate;
		this.user = user;
		this.items = items;
	}


	public CartDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
