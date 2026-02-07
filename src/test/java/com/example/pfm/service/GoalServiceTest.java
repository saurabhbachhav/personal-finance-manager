package com.example.pfm.service;

import com.example.pfm.dto.GoalDTO;
import com.example.pfm.model.Goal;
import com.example.pfm.model.Transaction;
import com.example.pfm.model.User;
import com.example.pfm.repository.GoalRepository;
import com.example.pfm.repository.TransactionRepository;
import com.example.pfm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {

     @Mock
     private GoalRepository goalRepository;

     @Mock
     private UserRepository userRepository;

     @Mock
     private TransactionRepository transactionRepository;

     @InjectMocks
     private GoalService goalService;

     private User user;
     private GoalDTO goalDTO;

     @BeforeEach
     void setUp() {
          user = new User();
          user.setId(1L);
          user.setUsername("test@example.com");

          goalDTO = new GoalDTO();
          goalDTO.setGoalName("Emergency Fund");
          goalDTO.setTargetAmount(BigDecimal.valueOf(5000));
          goalDTO.setTargetDate(LocalDate.now().plusMonths(6));
          goalDTO.setStartDate(LocalDate.now());
     }

     @Test
     void createGoal_Success() {
          when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

          Goal savedGoal = new Goal();
          savedGoal.setId(1L);
          savedGoal.setGoalName(goalDTO.getGoalName());
          savedGoal.setTargetAmount(goalDTO.getTargetAmount());
          savedGoal.setTargetDate(goalDTO.getTargetDate());
          savedGoal.setStartDate(goalDTO.getStartDate());
          savedGoal.setUser(user);

          when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);
          when(transactionRepository.findTransactions(any(), any(), any(), any(), any(), any(Sort.class)))
                    .thenReturn(Collections.emptyList());

          GoalDTO result = goalService.createGoal(user.getUsername(), goalDTO);

          assertNotNull(result);
          assertEquals("Emergency Fund", result.getGoalName());
          assertEquals(BigDecimal.ZERO, result.getCurrentProgress()); // No transactions
     }
}
