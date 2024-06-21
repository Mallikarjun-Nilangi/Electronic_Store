package com.lcwd.elestronic.store.Dto;

public class OrderItemDto {

	
    private int orderItemId;
	
	private int quantity;
	
	private int totalPrice;
	
	private ProductDto product;

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

	public OrderItemDto(int orderItemId, int quantity, int totalPrice, ProductDto product) {
		super();
		this.orderItemId = orderItemId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.product = product;
	}

	public OrderItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "OrderItemDto [orderItemId=" + orderItemId + ", quantity=" + quantity + ", totalPrice=" + totalPrice
				+ ", product=" + product + "]";
	}
	
	
}
