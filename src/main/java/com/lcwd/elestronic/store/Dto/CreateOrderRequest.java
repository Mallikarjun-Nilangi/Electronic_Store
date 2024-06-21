package com.lcwd.elestronic.store.Dto;

public class CreateOrderRequest {

	
	private String cartId;
	
	private String userId;
	
	private String orderStatus="PENDING";
	
	private String paymentStatus="NOTPAID";
	
	private String billingAddress;
	
	private String billingPhone;
	
	private String billingName;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingPhone() {
		return billingPhone;
	}

	public void setBillingPhone(String billingPhone) {
		this.billingPhone = billingPhone;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public CreateOrderRequest(String cartId, String userId, String orderStatus, String paymentStatus,
			String billingAddress, String billingPhone, String billingName) {
		super();
		this.cartId = cartId;
		this.userId = userId;
		this.orderStatus = orderStatus;
		this.paymentStatus = paymentStatus;
		this.billingAddress = billingAddress;
		this.billingPhone = billingPhone;
		this.billingName = billingName;
	}

	public CreateOrderRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	

	
}
