package com.lcwd.elestronic.store.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.elestronic.store.Entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
