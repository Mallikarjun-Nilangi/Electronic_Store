package com.lcwd.elestronic.store.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
