package com.example.pfm.controller;

import com.example.pfm.dto.CategoryDTO;
import com.example.pfm.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(Authentication authentication) {
        return ResponseEntity.ok(categoryService.getAllCategories(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> createCustomCategory(@RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        try {
            CategoryDTO createdCategory = categoryService.createCustomCategory(authentication.getName(), categoryDTO);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (RuntimeException e) {
             return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCustomCategory(@PathVariable String name, Authentication authentication) {
        try {
            categoryService.deleteCustomCategory(authentication.getName(), name);
            return ResponseEntity.ok(Collections.singletonMap("message", "Category deleted successfully"));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
