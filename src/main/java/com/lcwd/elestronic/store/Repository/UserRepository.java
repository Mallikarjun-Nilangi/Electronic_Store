package com.lcwd.elestronic.store.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.User;

public interface UserRepository extends JpaRepository<User, String> {
 
	Optional<User> findByEmail(String Email);
	
	Optional<User> findByEmailAndPassword(String Email,String Password);
	
	List<User> findByNameContaining(String keywords); 
	
}
