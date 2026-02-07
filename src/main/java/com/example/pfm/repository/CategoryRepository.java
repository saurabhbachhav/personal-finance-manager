package com.example.pfm.repository;

import com.example.pfm.model.Category;
import com.example.pfm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query("SELECT c FROM Category c WHERE c.user IS NULL OR c.user = :user")
    List<Category> findAllAvailableCategories(User user);

    List<Category> findByUser(User user);

    Optional<Category> findByNameAndUser(String name, User user);
    
    // For default categories
    Optional<Category> findByNameAndUserIsNull(String name);

    boolean existsByNameAndUser(String name, User user);
}
