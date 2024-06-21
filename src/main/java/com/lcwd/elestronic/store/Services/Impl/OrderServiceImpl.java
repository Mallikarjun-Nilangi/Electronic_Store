package com.lcwd.elestronic.store.Services.Impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.elestronic.store.Dto.CreateOrderRequest;
import com.lcwd.elestronic.store.Dto.OrderDto;
import com.lcwd.elestronic.store.Dto.PageableResponse;
import com.lcwd.elestronic.store.Entities.Cart;
import com.lcwd.elestronic.store.Entities.CartItem;
import com.lcwd.elestronic.store.Entities.Order;
import com.lcwd.elestronic.store.Entities.OrderItem;
import com.lcwd.elestronic.store.Entities.User;
import com.lcwd.elestronic.store.Exceptions.BadApiRequestException;
import com.lcwd.elestronic.store.Exceptions.ResourceNotFoundException;
import com.lcwd.elestronic.store.Helper.Helper;
import com.lcwd.elestronic.store.Repository.CartRepository;
import com.lcwd.elestronic.store.Repository.OrderRepository;
import com.lcwd.elestronic.store.Repository.UserRepository;
import com.lcwd.elestronic.store.Services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		
		String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();
		
		//fetch user
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with the given id !!"));
		
		//fetch cart
		
		Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("cart not found with the given id !!"));
		
		
		List<CartItem> cartitems = cart.getItems();
		
		if(cartitems.size() <= 0) {
			throw new BadApiRequestException("Invalid number of items in cart !");
		}
		
		Order order = new Order();
		
		order.setBillingName(orderDto.getBillingName());
		order.setBillingPhone(orderDto.getBillingPhone());
		order.setBillingAddress(orderDto.getBillingAddress());
		order.setDeliveredDate(null);
		order.setOrderedDate(new Date());
		order.setPaymentStatus(orderDto.getPaymentStatus());
		order.setOrderStatus(orderDto.getOrderStatus());
		order.setOrderId(UUID.randomUUID().toString());
		order.setUser(user);
		
		
		
		//orderItems , amount 
		
		
		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		
		
		
		List<OrderItem> orderItems = cartitems.stream().map(cartItem ->{
			
			//cartItems -> to -> orderItems
			
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setTotalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice());
			orderItem.setOrder(order);
			
			
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			
			return orderItem;
		}).collect(Collectors.toList());
		
		
		
		
		order.setOrderItems(orderItems);
		
		order.setOrderAmount(orderAmount.get());
		
		cart.getItems().clear();
		
		cartRepository.save(cart);
		
		Order savedOrder = orderRepository.save(order);
		
		
		return mapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		
		Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("order is not found with the given id !!"));
		
		orderRepository.delete(order);

	}

	@Override
	public List<OrderDto> getOrders(String userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with the given id !!"));
		
		List<Order> orders = orderRepository.findByUser(user);
		
		List<OrderDto> ordersDto = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
		
		return ordersDto;
	}

	@Override
	public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Order> page = orderRepository.findAll(pageable);
		
		return Helper.getPageableResponse(page, OrderDto.class);
	}

	@Override
	public OrderDto updateOrder(String orderId, OrderDto orderDto) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found with the given id"));
		order.setOrderStatus(orderDto.getOrderStatus());
		order.setPaymentStatus(orderDto.getPaymentStatus());
		order.setBillingAddress(orderDto.getBillingAddress());
		order.setBillingName(orderDto.getBillingName());
		order.setDeliveredDate(orderDto.getDeliveredDate());
		order.setBillingPhone(orderDto.getBillingPhone());
		order.setOrderAmount(orderDto.getOrderAmount());
		
		Order saveorder= orderRepository.save(order);
		
		return mapper.map(saveorder, OrderDto.class);
	}

}
