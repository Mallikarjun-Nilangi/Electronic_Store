package com.lcwd.elestronic.store.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.elestronic.store.Dto.ApiResponseMsg;
import com.lcwd.elestronic.store.Dto.CreateOrderRequest;
import com.lcwd.elestronic.store.Dto.OrderDto;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("orders")
@Tag(name = "OrderController",description = " REST APIs for related to perform Order operation !!")
public class OrderController {

	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest createOrderRequest){
		
		OrderDto order = orderService.createOrder(createOrderRequest);
		
		return new ResponseEntity<OrderDto>(order,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMsg> removeOrder(@PathVariable String orderId){
		
		orderService.removeOrder(orderId);
		
		ApiResponseMsg apiResponseMsg = new ApiResponseMsg();
		apiResponseMsg.setMessage("order is deleted successfully !");
		apiResponseMsg.setStatus(HttpStatus.OK);
		apiResponseMsg.setSuccess(true);
		return new ResponseEntity<>(apiResponseMsg, HttpStatus.OK);
	}
	
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrders(@PathVariable String userId){
		
		List<OrderDto> orders = orderService.getOrders(userId);
		
		return new ResponseEntity<List<OrderDto>>(orders, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
			 @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
			 @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			 @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
			 @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
			){
		PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<PageableResponse<OrderDto>>(allOrders,HttpStatus.OK);
	}
	
	
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, OrderDto orderDto){
		
		OrderDto updateOrder = orderService.updateOrder(orderId,orderDto);
		
		return new ResponseEntity<>(updateOrder,HttpStatus.OK);
	}
	
}
