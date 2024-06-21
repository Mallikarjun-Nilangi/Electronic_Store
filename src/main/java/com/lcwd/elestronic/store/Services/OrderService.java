package com.lcwd.elestronic.store.Services;

import java.util.List;

import com.lcwd.elestronic.store.Dto.CreateOrderRequest;
import com.lcwd.elestronic.store.Dto.OrderDto;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Entities.User;

public interface OrderService {

	//create order
	
	OrderDto createOrder(CreateOrderRequest orderDto);
	
	//remove order
	
	void removeOrder(String orderId);
	
	
	//get order by user
	
	List<OrderDto> getOrders(String userId);
	
	//get orders
	
	PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);

	OrderDto updateOrder(String orderId, OrderDto orderDto);
}
