package com.example.pfm.service;

import com.example.pfm.dto.CategoryDTO;
import com.example.pfm.model.Category;
import com.example.pfm.model.User;
import com.example.pfm.repository.CategoryRepository;
import com.example.pfm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

     private final CategoryRepository categoryRepository;
     private final UserRepository userRepository;

     public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
          this.categoryRepository = categoryRepository;
          this.userRepository = userRepository;
     }

     @PostConstruct
     public void initDefaultCategories() {
          createDefaultCategory("Salary", "INCOME");
          createDefaultCategory("Food", "EXPENSE");
          createDefaultCategory("Rent", "EXPENSE");
          createDefaultCategory("Transportation", "EXPENSE");
          createDefaultCategory("Entertainment", "EXPENSE");
          createDefaultCategory("Healthcare", "EXPENSE");
          createDefaultCategory("Utilities", "EXPENSE");
     }

     private void createDefaultCategory(String name, String type) {
          if (categoryRepository.findByNameAndUserIsNull(name).isEmpty()) {
               categoryRepository.save(new Category(name, type, null, false));
          }
     }

     public List<CategoryDTO> getAllCategories(String username) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          List<Category> categories = categoryRepository.findAllAvailableCategories(user); // Custom query needed or
                                                                                           // logic to combine
          // Actually, the requirement says "The system provides predefined categories...
          // Users can create custom categories".
          // It implies a user sees both.
          // My repo method findAllAvailableCategories(user) handles this: (user is null
          // OR user = :user)

          return categories.stream()
                    .map(c -> new CategoryDTO(c.getName(), c.getType(), c.isCustom()))
                    .collect(Collectors.toList());
     }

     @Transactional
     public CategoryDTO createCustomCategory(String username, CategoryDTO categoryDTO) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          if (!categoryDTO.getType().equals("INCOME") && !categoryDTO.getType().equals("EXPENSE")) {
               throw new IllegalArgumentException("Invalid category type. Must be INCOME or EXPENSE");
          }

          if (categoryRepository.existsByNameAndUser(categoryDTO.getName(), user) ||
                    categoryRepository.findByNameAndUserIsNull(categoryDTO.getName()).isPresent()) {
               throw new RuntimeException("Category already exists");
          }

          Category category = new Category();
          category.setName(categoryDTO.getName());
          category.setType(categoryDTO.getType());
          category.setUser(user);
          category.setCustom(true);

          categoryRepository.save(category);

          return new CategoryDTO(category.getName(), category.getType(), true);
     }

     @Transactional
     public void deleteCustomCategory(String username, String categoryName) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Category category = categoryRepository.findByNameAndUser(categoryName, user)
                    .orElseThrow(() -> new RuntimeException("Category not found or not custom"));

          // Check if referenced by transactions (ToDo: Implement check once Transaction
          // implemented)
          // For now, just delete
          categoryRepository.delete(category);
     }
}
