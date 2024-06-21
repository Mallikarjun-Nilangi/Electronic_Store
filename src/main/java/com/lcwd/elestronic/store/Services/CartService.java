package com.lcwd.elestronic.store.Services;

import com.lcwd.elestronic.store.Dto.AddItemToCartRequest;
import com.lcwd.elestronic.store.Dto.CartDto;

public interface CartService {
	
	//add items to cart
	//Case 1 : cart for user is not available : we will create the cart and then add the item
	//Case 2 : cart is available then add the items to cart
	
	CartDto addItemToCart(String userId,AddItemToCartRequest request);
	
	//remove item froom cart
	
	void removeItemFromCart(String userId,int cartItem);
	
	//remove all items from cart
	void clearCart(String userId);
	
	
	
	CartDto getCartByUser(String userId);

}
