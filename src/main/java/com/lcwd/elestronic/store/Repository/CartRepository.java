package com.lcwd.elestronic.store.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.Cart;
import com.lcwd.elestronic.store.Entities.User;

public interface CartRepository extends JpaRepository<Cart, String> {

	Optional<Cart> findByUser(User user);
}
