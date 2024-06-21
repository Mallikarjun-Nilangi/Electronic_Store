package com.lcwd.elestronic.store.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.Order;
import com.lcwd.elestronic.store.Entities.User;

public interface OrderRepository extends JpaRepository<Order, String> {

	
	List<Order> findByUser(User user);
}
