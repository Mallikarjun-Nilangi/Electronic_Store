package com.lcwd.elestronic.store.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

	
}
