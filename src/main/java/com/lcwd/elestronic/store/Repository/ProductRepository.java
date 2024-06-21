package com.lcwd.elestronic.store.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.Category;
import com.lcwd.elestronic.store.Entities.Product;


public interface ProductRepository extends JpaRepository<Product, String> {

	//search 
	
	Page<Product> findByTitleContaining(String title,Pageable pageable);
  
	Page<Product> findByLiveTrue(Pageable pageable);
	
	Page<Product> findByCategory(Category category,Pageable pageable);
}
