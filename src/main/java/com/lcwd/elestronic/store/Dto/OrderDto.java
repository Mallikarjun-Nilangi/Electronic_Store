package com.lcwd.elestronic.store.Dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderDto {

	
	private String orderId;
	
	private String orderStatus="PENDING";
	
	private String paymentStatus="NOTPAID";
	
	private int orderAmount;
	
	private String billingAddress;
	
	private String billingPhone;
	
	private String billingName;
	
	private Date orderedDate=new Date();
	
	private Date deliveredDate;
	
	
	private UserDto user;
	
	
	private List<OrderItemDto> orderItems = new ArrayList<>();


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


	public int getOrderAmount() {
		return orderAmount;
	}


	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
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


	public Date getOrderedDate() {
		return orderedDate;
	}


	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}


	public Date getDeliveredDate() {
		return deliveredDate;
	}


	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}


	public UserDto getUser() {
		return user;
	}


	public void setUser(UserDto user) {
		this.user = user;
	}


	public List<OrderItemDto> getOrderItems() {
		return orderItems;
	}


	public void setOrderItems(List<OrderItemDto> orderItems) {
		this.orderItems = orderItems;
	}


	


	public OrderDto() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public OrderDto(String orderId, String orderStatus, String paymentStatus, int orderAmount, String billingAddress,
			String billingPhone, String billingName, Date orderedDate, Date deliveredDate, UserDto user,
			List<OrderItemDto> orderItems) {
		super();
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.paymentStatus = paymentStatus;
		this.orderAmount = orderAmount;
		this.billingAddress = billingAddress;
		this.billingPhone = billingPhone;
		this.billingName = billingName;
		this.orderedDate = orderedDate;
		this.deliveredDate = deliveredDate;
		this.user = user;
		this.orderItems = orderItems;
	}


	@Override
	public String toString() {
		return "OrderDto [orderId=" + orderId + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus
				+ ", orderAmount=" + orderAmount + ", billingAddress=" + billingAddress + ", billingPhone="
				+ billingPhone + ", billingName=" + billingName + ", orderedDate=" + orderedDate + ", deliveredDate="
				+ deliveredDate + ", user=" + user + ", orderItems=" + orderItems + "]";
	}


	

	
	

}
