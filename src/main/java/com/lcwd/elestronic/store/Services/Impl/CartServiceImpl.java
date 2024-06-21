package com.lcwd.elestronic.store.Services.Impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.elestronic.store.Dto.AddItemToCartRequest;
import com.lcwd.elestronic.store.Dto.CartDto;
import com.lcwd.elestronic.store.Entities.Cart;
import com.lcwd.elestronic.store.Entities.CartItem;
import com.lcwd.elestronic.store.Entities.Product;
import com.lcwd.elestronic.store.Entities.User;
import com.lcwd.elestronic.store.Exceptions.BadApiRequestException;
import com.lcwd.elestronic.store.Exceptions.ResourceNotFoundException;
import com.lcwd.elestronic.store.Repository.CartItemRepository;
import com.lcwd.elestronic.store.Repository.CartRepository;
import com.lcwd.elestronic.store.Repository.ProductRepository;
import com.lcwd.elestronic.store.Repository.UserRepository;
import com.lcwd.elestronic.store.Services.CartService;

@Service
public class CartServiceImpl implements CartService {
	
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		
		int quantity = request.getQuantity();
		String productId = request.getProductId();
		
		
		if(quantity<=0) {
			throw new BadApiRequestException("Requested quantity is not valid !!");
		}
		
		//fetch the product
		
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with the given id !!"));
		
		//fetch user 
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with the given id !!"));
		
		Cart cart = null;
		
		try {
			cart = cartRepository.findByUser(user).get();
			
		} catch (NoSuchElementException e) {
			
			cart = new Cart();
			
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedDate(new Date());
		}
		
		//perform cart operation
		
		//if cart item already present then update
		
		AtomicReference<Boolean> updated = new AtomicReference<>(false);
		
		List<CartItem> items =cart.getItems();
		
		
		items =	items.stream().map(item -> {
			
			if(item.getProduct().getProductId().equals(productId)) {
				//item already present in cart
				item.setQuantity(quantity);
				item.setTotalPrice(quantity*product.getDiscountedPrice());
				updated.set(true);
			}
			
			return item;
		}).collect(Collectors.toList());
		
		
		cart.setItems(items);
		
		//create items
		
       
		if(!updated.get()) {
			 CartItem cartItem = new CartItem();
		        cartItem.setQuantity(quantity);
		        cartItem.setTotalPrice(quantity*product.getDiscountedPrice());
		        cartItem.setCart(cart);
		        cartItem.setProduct(product);
		        
		        cart.getItems().add(cartItem);
		}
		
        
        cart.setUser(user);
        
        Cart updatedCart = cartRepository.save(cart);
        
		
		
		return mapper.map(updatedCart, CartDto.class);
	}
	
	
	

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
		
		
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("cartItem not found with given id !!"));
		
		cartItemRepository.delete(cartItem);

	}

	@Override
	public void clearCart(String userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with the given id !!"));
		
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of given user not found !!"));
		
		cart.getItems().clear();
		
		cartRepository.save(cart);

	}




	@Override
	public CartDto getCartByUser(String userId) {
		
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with the given id !!"));
		
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of given user not found !!"));
		
		return mapper.map(cart, CartDto.class);
	}

}
