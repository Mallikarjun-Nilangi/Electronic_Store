package com.lcwd.elestronic.store.Dto;


public class AddItemToCartRequest {

     private String productId;	
     
     private int quantity;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public AddItemToCartRequest(String productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}

	public AddItemToCartRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
     
     
}
