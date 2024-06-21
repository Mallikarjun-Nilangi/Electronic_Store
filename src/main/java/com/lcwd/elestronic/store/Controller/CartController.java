package com.lcwd.elestronic.store.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.elestronic.store.Dto.AddItemToCartRequest;
import com.lcwd.elestronic.store.Dto.ApiResponseMsg;
import com.lcwd.elestronic.store.Dto.CartDto;
import com.lcwd.elestronic.store.Services.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carts")
@Tag(name = "CartController",description = " APIs for related to perform cart operation !!")
public class CartController {

	
	@Autowired
	private CartService cartService;
	
	
	// add items to cart
	
	
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addCartItems(@PathVariable String userId,@RequestBody AddItemToCartRequest cartRequest){
		
		CartDto cartDto = cartService.addItemToCart(userId, cartRequest);
		
		return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMsg> removeItemFromCart(@PathVariable String userId,@PathVariable int itemId){
		
		cartService.removeItemFromCart(userId, itemId);
		
		ApiResponseMsg apiResponseMsg = new ApiResponseMsg();
		apiResponseMsg.setMessage("item is removed !!");
		apiResponseMsg.setSuccess(true);
		apiResponseMsg.setStatus(HttpStatus.OK);
		
		return new ResponseEntity<ApiResponseMsg>(apiResponseMsg,HttpStatus.OK);
	}
	
	
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMsg> clearCart(@PathVariable String userId){
		
	     cartService.clearCart(userId);
		
		ApiResponseMsg apiResponseMsg = new ApiResponseMsg();
		apiResponseMsg.setMessage("cart is cleared !!");
		apiResponseMsg.setSuccess(true);
		apiResponseMsg.setStatus(HttpStatus.OK);
		
		return new ResponseEntity<ApiResponseMsg>(apiResponseMsg,HttpStatus.OK);
	}
	
	
	
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> addCartItems(@PathVariable String userId){
		
		CartDto cartDto = cartService.getCartByUser(userId);
		
		return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
	}
	
}
