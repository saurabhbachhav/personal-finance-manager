package com.example.pfm.controller;

import com.example.pfm.dto.GoalDTO;
import com.example.pfm.service.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

     private final GoalService goalService;

     public GoalController(GoalService goalService) {
          this.goalService = goalService;
     }

     @PostMapping
     public ResponseEntity<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO, Authentication authentication) {
          return new ResponseEntity<>(goalService.createGoal(authentication.getName(), goalDTO), HttpStatus.CREATED);
     }

     @GetMapping
     public ResponseEntity<List<GoalDTO>> getGoals(Authentication authentication) {
          return ResponseEntity.ok(goalService.getGoals(authentication.getName()));
     }

     @GetMapping("/{id}")
     public ResponseEntity<GoalDTO> getGoal(@PathVariable Long id, Authentication authentication) {
          return ResponseEntity.ok(goalService.getGoal(authentication.getName(), id));
     }

     @PutMapping("/{id}")
     public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody GoalDTO goalDTO,
               Authentication authentication) {
          try {
               return ResponseEntity.ok(goalService.updateGoal(authentication.getName(), id, goalDTO));
          } catch (RuntimeException e) {
               return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
          }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<?> deleteGoal(@PathVariable Long id, Authentication authentication) {
          try {
               goalService.deleteGoal(authentication.getName(), id);
               return ResponseEntity.ok(Collections.singletonMap("message", "Goal deleted successfully"));
          } catch (RuntimeException e) {
               return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
          }
     }
}
