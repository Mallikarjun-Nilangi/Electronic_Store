package com.lcwd.elestronic.store.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
